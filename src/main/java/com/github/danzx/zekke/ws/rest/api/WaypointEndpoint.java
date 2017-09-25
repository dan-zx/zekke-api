/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.ws.rest.api;

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.ws.rest.ApiVersions.V_1;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.danzx.zekke.constraint.NullId;
import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.service.WaypointService;
import com.github.danzx.zekke.service.WaypointService.NearWaypointsQuery;
import com.github.danzx.zekke.service.WaypointService.WaypointsQuery;
import com.github.danzx.zekke.transformer.Transformer;
import com.github.danzx.zekke.ws.rest.MediaTypes;
import com.github.danzx.zekke.ws.rest.PATCH;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;
import com.github.danzx.zekke.ws.rest.patch.ObjectPatch;
import com.github.danzx.zekke.ws.rest.security.RequireRoleAccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * Waypoints endpoint. Only authenticated user can use this endpoint
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@Path(V_1 + "/waypoints")
@RequireRoleAccess(roleRequired = User.Role.ANONYMOUS)
public class WaypointEndpoint {

    private static final Logger log = LoggerFactory.getLogger(WaypointEndpoint.class);

    private final WaypointService waypointService;
    private final Transformer<Waypoint, Poi> waypointToPoiTransformer;
    private final Transformer<Waypoint, Walkway> waypointToWalkwayTransformer;
    private final Transformer<Waypoint, TypedWaypoint> waypointToTypedWaypointTransformer;

    public @Inject WaypointEndpoint(WaypointService waypointService,
            Transformer<Waypoint, Poi> waypointToPoiTransformer,
            Transformer<Waypoint, Walkway> waypointToWalkwayTransformer,
            Transformer<Waypoint, TypedWaypoint> waypointToTypedWaypointTransformer) {
        this.waypointService = requireNonNull(waypointService);
        this.waypointToPoiTransformer = requireNonNull(waypointToPoiTransformer);
        this.waypointToWalkwayTransformer = requireNonNull(waypointToWalkwayTransformer);
        this.waypointToTypedWaypointTransformer = requireNonNull(waypointToTypedWaypointTransformer);
    }

    /**
     * TypedWaypoint collection.
     * 
     * @param bbox If present, finds all the waypoints within a rectangle specified by a latitude
     *        and longitude pair being the first the bottom left coordinates and the second the
     *        upper right coordinates. Example: '12.23,32.681;15.234,37.65'
     * @return a list of TypedWaypoint or an empty list.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TypedWaypoint> getTypedWaypoints(@Valid @QueryParam("bbox") BoundingBox bbox) {
        log.info("GET /waypoints?bbox={}", bbox);
        List<Waypoint> waypoints = queryWaypoints(null, bbox, null);
        return waypointToTypedWaypointTransformer.convertListAtoListB(waypoints);
    }

    /**
     * POIs collection.
     * 
     * @param bbox If present, finds all the waypoints within a rectangle specified by a latitude
     *        and longitude pair being the first the bottom left coordinates and the second the
     *        upper right coordinates. Example: '12.23,32.681;15.234,37.65'
     * @param queryStr If present, filter by POI names that contain this string.
     * @return a list of POIs or an empty list.
     */
    @GET
    @Path("/pois")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Poi> getPois(
            @Valid @QueryParam("bbox") BoundingBox bbox,
            @QueryParam("query") String queryStr) {
        log.info("GET /waypoints/pois?bbox={}&query={}", bbox, queryStr);
        List<Waypoint> waypoints = queryWaypoints(Type.POI, bbox, queryStr);
        return waypointToPoiTransformer.convertListAtoListB(waypoints);
    }

    /**
     * Walkways collection.
     * 
     * @param bbox If present, finds all the waypoints within a rectangle specified by a latitude
     *        and longitude pair being the first the bottom left coordinates and the second the
     *        upper right coordinates. Example: '12.23,32.681;15.234,37.65'
     * @return a list of walkways or an empty list.
     */
    @GET
    @Path("/walkways")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Walkway> getWalkways(@Valid @QueryParam("bbox") BoundingBox bbox) {
        log.info("GET /waypoints/walkways?bbox={}", bbox);
        List<Waypoint> waypoints = queryWaypoints(Type.WALKWAY, bbox, null);
        return waypointToWalkwayTransformer.convertListAtoListB(waypoints);
    }

    /**
     * Creates a new waypoint. Requires an admin use this endpoint.
     * 
     * @param typedWaypoint a TypedWaypoint.
     * @return the same TypedWaypoint with an id.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequireRoleAccess(roleRequired = User.Role.ADMIN)
    public TypedWaypoint newWaypoint(@NotNull @NullId @Valid TypedWaypoint typedWaypoint) {
        log.info("POST /waypoints -- body: {}", typedWaypoint);
        Waypoint waypoint = waypointToTypedWaypointTransformer.convertBtoA(typedWaypoint);
        waypointService.persist(waypoint);
        typedWaypoint.setId(waypoint.getId());
        return typedWaypoint;
    }

    /**
     * POI name completion.
     * 
     * @param bbox If present, finds all the waypoints within a rectangle specified by a latitude
     *        and longitude pair being the first the bottom left coordinates and the second the
     *        upper right coordinates. Example: '12.23,32.681;15.234,37.65'
     * @param queryStr If present, filter by POI names that contain this string.
     * @return a list of POIs with only its name and id or an empty list.
     */
    @GET
    @Path("/pois/suggestnames")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Poi> getPoiSuggestions(
            @Valid @QueryParam("bbox") BoundingBox bbox, 
            @QueryParam("query") String queryStr) {
        log.info("GET /waypoints/pois/suggestnames?bbox={}&query={}", bbox);
        List<Waypoint> pois = waypointService.findPoisForNameCompletion(bbox, queryStr);
        return waypointToPoiTransformer.convertListAtoListB(pois);
    }

    /**
     * Near TypedWaypoints.
     * 
     * @param location The center point to use. Example: '11.432,53.645'
     * @param distance Limits the results to those waypoints that are at most the specified distance
     *        from the center point.
     * @param limit Limits the results to the given number.
     * @return a list of TypedWaypoint or an empty list.
     */
    @GET
    @Path("/near")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TypedWaypoint> getNearTypedWaypoints(
            @NotNull @Valid @QueryParam("location") Coordinates location,
            @QueryParam("distance") Integer distance,
            @QueryParam("limit") Integer limit) {
        log.info("GET /waypoints/near?location={}&distance={}&limit={}", location, distance, limit);
        List<Waypoint> waypoints = queryNearWaypoints(null, location, distance, limit);
        return waypointToTypedWaypointTransformer.convertListAtoListB(waypoints);
    }

    /**
     * Near POIs.
     * 
     * @param location The center point to use. Example: '11.432,53.645'
     * @param distance Limits the results to those waypoints that are at most the specified distance
     *        from the center point.
     * @param limit Limits the results to the given number.
     * @return a list of POIs or an empty list.
     */
    @GET
    @Path("/pois/near")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Poi> getNearPois(
            @NotNull @Valid @QueryParam("location") Coordinates location,
            @QueryParam("distance") Integer distance,
            @QueryParam("limit") Integer limit) {
        log.info("GET /waypoints/pois/near?location={}&distance={}&limit={}", location, distance, limit);
        List<Waypoint> waypoints = queryNearWaypoints(Type.POI, location, distance, limit);
        return waypointToPoiTransformer.convertListAtoListB(waypoints);
    }

    /**
     * Near Walkways.
     * 
     * @param location The center point to use. Example: '11.432,53.645'
     * @param distance Limits the results to those waypoints that are at most the specified distance
     *        from the center point.
     * @param limit Limits the results to the given number.
     * @return a list of walkways or an empty list.
     */
    @GET
    @Path("/walways/near")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Walkway> getNearWalways(
            @NotNull @Valid @QueryParam("location") Coordinates location,
            @QueryParam("distance") Integer distance,
            @QueryParam("limit") Integer limit) {
        log.info("GET /waypoints/walkways?location={}&distance={}&limit={}", location, distance, limit);
        List<Waypoint> waypoints = queryNearWaypoints(Type.WALKWAY, location, distance, limit);
        return waypointToWalkwayTransformer.convertListAtoListB(waypoints);
    }

    /**
     * Finds a TypedWaypoint by it's id.
     * 
     * @param id an id.
     * @return a TypedWaypoint or 404 Not Found.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWaypoint(@NotNull @PathParam("id") Long id) {
        log.info("GET /waypoints/{}", id);
        return waypointService.findWaypointById(id)
                .map(waypoint -> waypointToTypedWaypointTransformer.convertAtoB(waypoint))
                .map(typedWaypoint -> Response.ok(typedWaypoint).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    /**
     * Deletes a waypoint by it's id. Requires an admin use this endpoint.
     * 
     * @param id an id.
     * @return a 204 No Content or 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    @RequireRoleAccess(roleRequired = User.Role.ADMIN)
    public Response deleteWaypoint(@NotNull @PathParam("id") Long id) {
        log.info("DELETE /waypoints/{}", id);
        Waypoint waypoint = new Waypoint();
        waypoint.setId(id);
        return waypointService.delete(waypoint) ? 
                Response.noContent().build() : 
                Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Patches a waypoint by it's id. Requires an admin use this endpoint.
     * 
     * @param id an id.
     * @param patch the update.
     * @return a 200 OK with the updated waypoint or 404 Not Found.
     */
    @PATCH
    @Path("/{id}")
    @Consumes(MediaTypes.APPLICATION_JSON_PATCH)
    @Produces(MediaType.APPLICATION_JSON)
    @RequireRoleAccess(roleRequired = User.Role.ADMIN)
    public Response patchWaypoint(@NotNull @PathParam("id") Long id, @NotNull ObjectPatch patch) {
        log.info("PATCH /waypoints/{} -- body: {}", id, patch);
        Optional<Waypoint> optWaypoint = waypointService.findWaypointById(id);
        return optWaypoint
            .map(waypointToTypedWaypointTransformer::convertAtoB)
            .map(patch::apply)
            .map(typedWaypoint -> {
                    typedWaypoint.setId(id);
                    Waypoint waypoint = waypointToTypedWaypointTransformer.convertBtoA(typedWaypoint);
                    waypointService.persist(waypoint);
                    return typedWaypoint;
                })
            .map(typedWaypoint -> Response.ok(typedWaypoint).build())
            .orElse(Response.status(Status.NOT_FOUND).build());
    }

    private List<Waypoint> queryWaypoints(Type waypointType, BoundingBox bbox,String queryStr) {
        WaypointsQuery query = new WaypointsQuery.Builder()
                .withinBoundingBox(bbox)
                .ofType(waypointType)
                .withNameContaining(queryStr)
                .build();
        return waypointService.findWaypoints(query);
    }

    private List<Waypoint> queryNearWaypoints(Type waypointType, Coordinates location, Integer distance, Integer limit) {
        NearWaypointsQuery query = NearWaypointsQuery.Builder
                .nearLocation(location)
                .byType(waypointType)
                .limitResulsTo(limit)
                .maximumSearchDistance(distance)
                .build();
        return waypointService.findNearWaypoints(query);
    }
}
