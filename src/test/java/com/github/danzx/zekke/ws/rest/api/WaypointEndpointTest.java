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

import static java.util.Collections.emptyList;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import static com.github.danzx.zekke.test.assertion.ProjectAssertions.assertThat;
import static com.github.danzx.zekke.ws.rest.MediaTypes.APPLICATION_JSON_PATCH_TYPE;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.ws.rs.core.Response;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.jersey.BaseJerseyTest;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;

import org.junit.Test;

public class WaypointEndpointTest extends BaseJerseyTest {

    private static final String ENDPOINT_BASE_PATH = "v1/waypoints";
    private String anonymousToken;
    private String adminToken;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        anonymousToken = getJwtFactory().newToken(User.Role.ANONYMOUS);
        adminToken = getJwtFactory().newToken(User.Role.ADMIN);
        disableSecurity();
    }

    @Test
    public void shouldGetTypedWaypointsFailValidationWhenBboxNotNullButInvalid() {
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
        Response response = target("v1/waypoints")
                .queryParam("bbox", bbox)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetTypedWaypointsWontFail() {
        when(getMockWaypointService().findWaypoints(any())).thenReturn(emptyList());

        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(TypedWaypoint.class)
                    .isEmpty();
    }

    @Test
    public void shouldGetWalkwaysFailValidationWhenBboxNotNullButInvalid() throws Exception {
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
        Response response = target("v1/waypoints/walkways")
                .queryParam("bbox", bbox)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetWalkwayWontFail() {
        when(getMockWaypointService().findWaypoints(any())).thenReturn(emptyList());

        Response response = target("v1/waypoints/walkways")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(Walkway.class)
                    .isEmpty();
    }

    @Test
    public void shouldGetPoisFailValidationWhenBboxIsNotNullButInvalid() throws Exception {
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
        Response response = target("v1/waypoints/pois")
                .queryParam("bbox", bbox)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetPoisWontFail() {
        when(getMockWaypointService().findWaypoints(any())).thenReturn(emptyList());
        Response response = target("v1/waypoints/pois")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(Poi.class)
                    .isEmpty();
    }

    @Test
    public void shouldNewWaypointWontFail() {
        final long newId = 12L;
        TypedWaypoint payload = new TypedWaypoint();
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        doAnswer(invocation -> {
            Waypoint waypoint = invocation.getArgumentAt(0, Waypoint.class);
            waypoint.setId(newId);
            return null;
        }).when(getMockWaypointService()).persist(any());

        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(json(payload));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAs(TypedWaypoint.class)
                    .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
                    .containsOnly(newId, payload.getName(), payload.getLocation(), payload.getType());
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadIsNull() throws Exception {
        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(json(null));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadRequiredFieldsAreNull() throws Exception {
        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(json(new TypedWaypoint()));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldNewWaypointFailValidationWhenIdIsNotNull() throws Exception {
        TypedWaypoint payload = new TypedWaypoint();
        payload.setId(1L);
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(12.43, 43.5));

        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(json(payload));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadLocationIsNotNullButInvalid() throws Exception {
        TypedWaypoint payload = new TypedWaypoint();
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(1111d, 12313d));

        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(json(payload));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetPoiSuggestionsFailValidationWhenBboxIsNotNullButInvalid() throws Exception {
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
        Response response = target("v1/waypoints/pois/names")
                .queryParam("bbox", bbox)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetPoiSuggestionsWontFail() {
        when(getMockWaypointService().findWaypoints(any())).thenReturn(emptyList());
        Response response = target("v1/waypoints/pois/names")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(Poi.class)
                    .isEmpty();
    }

    @Test
    public void shouldGetNearTypedWaypointsFailValidationWhenCoordiantesIsNull() throws Exception {
        Response response = target("v1/waypoints/near")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetNearTypedWaypointsFailValidationWhenCoordiantesIsNotNullButInvalid() throws Exception {
        Coordinates location = Coordinates.ofLatLng(1111d, 12313d);
        Response response = target("v1/waypoints/near")
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetNearTypedWaypointsWontFail() {
        when(getMockWaypointService().findWaypointsNearALocation(any())).thenReturn(emptyList());
        Coordinates location = Coordinates.ofLatLng(12.43, 43.5);
        Response response = target("v1/waypoints/near")
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(TypedWaypoint.class)
                    .isEmpty();
    }

    @Test
    public void shouldGetNearPoisFailValidationWhenCoordiantesIsNull() throws Exception {
        Response response = target("v1/waypoints/pois/near")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetNearPoisFailValidationWhenCoordiantesIsNotNullButInvalid() throws Exception {
        Coordinates location = Coordinates.ofLatLng(1111d, 12313d);
        Response response = target("v1/waypoints/pois/near")
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetNearPoisWontFail() {
        when(getMockWaypointService().findWaypointsNearALocation(any())).thenReturn(emptyList());
        Coordinates location = Coordinates.ofLatLng(12.43, 43.5);
        Response response = target("v1/waypoints/pois/near")
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(Poi.class)
                    .isEmpty();
    }

    @Test
    public void shouldGetNearWalwaysFailValidationWhenCoordiantesIsNull() throws Exception {
        Response response = target("v1/waypoints/walkways/near")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetNearWalwaysFailValidationWhenCoordiantesIsNotNullButInvalid() throws Exception {
        Coordinates location = Coordinates.ofLatLng(1111d, 12313d);
        Response response = target("v1/waypoints/walkways/near")
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .hasEntity();
    }

    @Test
    public void shouldGetNearWalkwaysWontFail() {
        when(getMockWaypointService().findWaypointsNearALocation(any())).thenReturn(emptyList());
        Coordinates location = Coordinates.ofLatLng(12.43, 43.5);
        Response response = target("v1/waypoints/walkways/near")
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(Poi.class)
                    .isEmpty();
    }

    @Test
    public void shouldGetWaypointRespondWithData() {
        TypedWaypoint responseBody = new TypedWaypoint();
        responseBody.setId(2L);
        responseBody.setName("A Name");
        responseBody.setType(Type.POI);
        responseBody.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        
        Waypoint waypoint = new Waypoint();
        waypoint.setId(responseBody.getId());
        waypoint.setLocation(responseBody.getLocation());
        waypoint.setName(responseBody.getName());
        waypoint.setType(responseBody.getType());
        
        when(getMockWaypointService().findWaypointById(responseBody.getId())).thenReturn(Optional.of(waypoint));

        Response response = target("v1/waypoints/{id}")
                .resolveTemplate("id", responseBody.getId())
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAs(TypedWaypoint.class)
                    .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
                    .containsOnly(responseBody.getId(), responseBody.getName(), responseBody.getLocation(), responseBody.getType());
    }

    @Test
    public void shouldGetWaypointRespondWithNotFoundWhenNoData() {
        final long id = 5L;
        when(getMockWaypointService().findWaypointById(id)).thenReturn(Optional.empty());

        Response response = target("v1/waypoints/{id}")
                .resolveTemplate("id", id)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(NOT_FOUND)
                .extractingEntityAs(ErrorMessage.class)
                    .extracting(ErrorMessage::getErrorType, ErrorMessage::getStatusCode, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
                    .containsOnly(ErrorMessage.Type.NOT_FOUND, NOT_FOUND.getStatusCode(), "Resource not found", null);
    }

    @Test
    public void shouldDeleteWaypointRespondWithNoContent() {
        when(getMockWaypointService().delete(any())).thenReturn(true);
        Response response = target("v1/waypoints/{id}")
                .resolveTemplate("id", 5L)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response)
                .respondedWith(NO_CONTENT)
                .doesNotContainEntity();
    }

    @Test
    public void shouldDeleteWaypointRespondWithNotFoundWhenNoData() {
        when(getMockWaypointService().delete(any())).thenReturn(false);

        Response response = target("v1/waypoints/{id}")
                .resolveTemplate("id", 5L)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(NOT_FOUND)
                .extractingEntityAs(ErrorMessage.class)
                    .extracting(ErrorMessage::getErrorType, ErrorMessage::getStatusCode, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
                    .containsOnly(ErrorMessage.Type.NOT_FOUND, NOT_FOUND.getStatusCode(), "Resource not found", null);
    }

    @Test
    public void shouldPatchWaypointRespondWithNotFoundWhenNoDataIsFound() throws Exception {
        when(getMockWaypointService().findWaypointById(anyLong())).thenReturn(Optional.empty());

        Response response = target("v1/waypoints/{id}")
                .resolveTemplate("id", 5L)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .method("PATCH", entity(dummyPatch(), APPLICATION_JSON_PATCH_TYPE));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(NOT_FOUND)
                .extractingEntityAs(ErrorMessage.class)
                    .extracting(ErrorMessage::getErrorType, ErrorMessage::getStatusCode, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
                    .containsOnly(ErrorMessage.Type.NOT_FOUND, NOT_FOUND.getStatusCode(), "Resource not found", null);

        verify(getMockWaypointService(), never()).persist(any());
    }

    @Test
    public void shouldPatchWaypointRespondOkWithBodyWhenDataIsFoundAndPatched() throws Exception {
        long id = 1L;
        TypedWaypoint responseBody = new TypedWaypoint();
        responseBody.setId(id);
        responseBody.setName("A Name");
        responseBody.setType(Type.POI);
        responseBody.setLocation(Coordinates.ofLatLng(12.43, 43.5));

        Waypoint waypoint = new Waypoint();
        waypoint.setId(responseBody.getId());
        waypoint.setLocation(responseBody.getLocation());
        waypoint.setName(responseBody.getName());
        waypoint.setType(responseBody.getType());

        when(getMockWaypointService().findWaypointById(id)).thenReturn(Optional.of(waypoint));

        Response response = target("v1/waypoints/{id}")
                .resolveTemplate("id", id)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .method("PATCH", entity(dummyPatch(), APPLICATION_JSON_PATCH_TYPE));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAs(TypedWaypoint.class)
                    .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
                    .containsOnly(responseBody.getId(), responseBody.getName(), responseBody.getLocation(), responseBody.getType());

        verify(getMockWaypointService()).persist(waypoint);
    }

    private String dummyPatch() throws Exception {
        return "[]";
    }
}
