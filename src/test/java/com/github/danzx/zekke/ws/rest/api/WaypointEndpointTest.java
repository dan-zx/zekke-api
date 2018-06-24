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
import static java.util.Collections.singletonList;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import static com.github.danzx.zekke.test.assertion.ProjectAssertions.assertThat;
import static com.github.danzx.zekke.ws.rest.MediaTypes.APPLICATION_JSON_PATCH_TYPE;
import static com.github.danzx.zekke.ws.rest.model.ErrorMessage.Type.PARAM_VALIDATION;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.jersey.BaseJerseyTest;
import com.github.danzx.zekke.ws.rest.model.BaseWaypoint;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters(method = "endpointsThatReturnAllTestParams")
    public void shouldEndpointsThatReturnAllWontFailWhenServiceDoNotFail(String endpoint, List<Waypoint> serviceList, List<? extends BaseWaypoint> expectedPayload, Class<? extends BaseWaypoint> payloadType) {
        when(getMockWaypointService().findWaypoints(any())).thenReturn(serviceList);
        Response response = target(endpoint)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(payloadType)
                    .isEqualTo(expectedPayload);
    }

    public Object[] endpointsThatReturnAllTestParams() {
        Waypoint waypoint = new Waypoint();
        waypoint.setId(1L);
        waypoint.setName("A name");
        waypoint.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        waypoint.setType(Type.POI);

        TypedWaypoint typedWaypoint = new TypedWaypoint();
        typedWaypoint.setId(waypoint.getId());
        typedWaypoint.setName(waypoint.getName().get());
        typedWaypoint.setLocation(waypoint.getLocation());
        typedWaypoint.setType(waypoint.getType());

        Object[] test1 = { "v1/waypoints", singletonList(waypoint), singletonList(typedWaypoint), TypedWaypoint.class };

        waypoint = new Waypoint();
        waypoint.setId(2L);
        waypoint.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        waypoint.setType(Type.WALKWAY);

        Walkway walkway = new Walkway();
        walkway.setId(waypoint.getId());
        walkway.setLocation(waypoint.getLocation());

        Object[] test2 = { "v1/waypoints/walkways", singletonList(waypoint), singletonList(walkway), Walkway.class };

        waypoint = new Waypoint();
        waypoint.setId(1L);
        waypoint.setName("A name");
        waypoint.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        waypoint.setType(Type.POI);

        Poi poi = new Poi();
        poi.setId(waypoint.getId());
        poi.setName(waypoint.getName().get());
        poi.setLocation(waypoint.getLocation());

        Object[] test3 = { "v1/waypoints/pois", singletonList(waypoint), singletonList(poi), Poi.class };

        waypoint = new Waypoint();
        waypoint.setId(1L);
        waypoint.setName("A name");
        waypoint.setType(Type.POI);

        Poi poiNames = new Poi();
        poiNames.setId(waypoint.getId());
        poiNames.setName(waypoint.getName().get());

        Object[] test4 = { "v1/waypoints/pois/names", singletonList(waypoint), singletonList(poiNames), Poi.class };

        return new Object[][] {
                test1, test2, test3, test4,
                { "v1/waypoints", emptyList(), emptyList(), TypedWaypoint.class },
                { "v1/waypoints/walkways", emptyList(), emptyList(), Walkway.class },
                { "v1/waypoints/pois", emptyList(), emptyList(), Poi.class },
                { "v1/waypoints/pois/names", emptyList(), emptyList(), Poi.class },
        };
    }

    @Test
    @Parameters(method = "shouldEndpointsThatReturnListBasedOnLocationTestParams")
    public void shouldEndpointsThatReturnListBasedOnLocationWontFailWhenServiceDoNotFail(String endpoint, List<Waypoint> serviceList, List<? extends BaseWaypoint> expectedPayload, Class<? extends BaseWaypoint> payloadType) {
        when(getMockWaypointService().findWaypointsNearALocation(any())).thenReturn(serviceList);
        Coordinates location = Coordinates.ofLatLng(12.43, 43.5);
        Response response = target(endpoint)
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAsListOf(payloadType)
                    .isEqualTo(expectedPayload);
    }

    public Object[] shouldEndpointsThatReturnListBasedOnLocationTestParams() {
        Waypoint waypoint = new Waypoint();
        waypoint.setId(1L);
        waypoint.setName("A name");
        waypoint.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        waypoint.setType(Type.POI);

        TypedWaypoint typedWaypoint = new TypedWaypoint();
        typedWaypoint.setId(waypoint.getId());
        typedWaypoint.setName(waypoint.getName().get());
        typedWaypoint.setLocation(waypoint.getLocation());
        typedWaypoint.setType(waypoint.getType());

        Object[] test1 = { "v1/waypoints/near", singletonList(waypoint), singletonList(typedWaypoint), TypedWaypoint.class };

        waypoint = new Waypoint();
        waypoint.setId(2L);
        waypoint.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        waypoint.setType(Type.WALKWAY);

        Walkway walkway = new Walkway();
        walkway.setId(waypoint.getId());
        walkway.setLocation(waypoint.getLocation());

        Object[] test2 = { "v1/waypoints/walkways/near", singletonList(waypoint), singletonList(walkway), Walkway.class };

        waypoint = new Waypoint();
        waypoint.setId(1L);
        waypoint.setName("A name");
        waypoint.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        waypoint.setType(Type.POI);

        Poi poi = new Poi();
        poi.setId(waypoint.getId());
        poi.setName(waypoint.getName().get());
        poi.setLocation(waypoint.getLocation());

        Object[] test3 = { "v1/waypoints/pois/near", singletonList(waypoint), singletonList(poi), Poi.class };

        return new Object[][] {
                test1, test2, test3,
                { "v1/waypoints/near", emptyList(), emptyList(), TypedWaypoint.class },
                { "v1/waypoints/walkways/near", emptyList(), emptyList(), Walkway.class },
                { "v1/waypoints/pois/near", emptyList(), emptyList(), Poi.class },
        };
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
    public void shouldNewWaypointWontFailWhenPayloadIsValid() {
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
                .method("PATCH", entity("[]", APPLICATION_JSON_PATCH_TYPE));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAs(TypedWaypoint.class)
                    .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
                    .containsOnly(responseBody.getId(), responseBody.getName(), responseBody.getLocation(), responseBody.getType());

        verify(getMockWaypointService()).persist(waypoint);
    }

    @Test
    @Parameters({
            "v1/waypoints",
            "v1/waypoints/walkways",
            "v1/waypoints/pois",
            "v1/waypoints/pois/names"
    })
    public void shouldEndpointFailValidationWhenBboxNotNullButInvalid(String endpoint) {
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
        Response response = target(endpoint)
                .queryParam("bbox", bbox)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        ErrorMessage expectedError = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("topCoordinates.latitude", "must be between -90.0 and 90.0")
                .addParamError("topCoordinates.longitude", "must be between -180.0 and 180.0")
                .addParamError("bottomCoordinates.latitude", "must be between -90.0 and 90.0")
                .addParamError("bottomCoordinates.longitude", "must be between -180.0 and 180.0")
                .build();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .extractingEntityAs(ErrorMessage.class)
                    .isEqualTo(expectedError);
    }

    @Test
    @Parameters(method = "invalidNewWaypointsAndErrorsProduced")
    public void shouldNewWaypointFailValidationWhenPayloadIsNotValid(TypedWaypoint payload, ErrorMessage expectedError) throws Exception {
        Response response = target("v1/waypoints")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .post(json(payload));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .extractingEntityAs(ErrorMessage.class)
                    .isEqualTo(expectedError);
    }

    public Object[] invalidNewWaypointsAndErrorsProduced() {
        ErrorMessage expectedError = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("arg0", "may not be null")
                .build();
        Object[] test1 = { null, expectedError }; // null


        expectedError = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("type", "may not be null")
                .addParamError("location", "may not be null")
                .build();
        Object[] test2 = { new TypedWaypoint(), expectedError }; // misses all required values

        TypedWaypoint payload = new TypedWaypoint();
        payload.setId(1L);
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        expectedError = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("id", "must be null")
                .build();
        Object[] test3 = { payload, expectedError }; // id is not null

        payload = new TypedWaypoint();
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(1111d, 12313d));
        expectedError = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("location.latitude", "must be between -90.0 and 90.0")
                .addParamError("location.longitude", "must be between -180.0 and 180.0")
                .build();
        Object[] test4 = { payload, expectedError }; // bad location

        return new Object[] { test1, test2, test3, test4 };
    }

    @Test
    @Parameters(method = "endpointsRequiringLocationsInvalidLocationsAndErrorsProduced")
    public void shouldEndpointsRequiringLocationFailValidationWhenCoordiantesAreNotValid(String endpoint, String location, ErrorMessage expectedErrorMessage) throws Exception {
        Response response = target(endpoint)
                .queryParam("location", location)
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .extractingEntityAs(ErrorMessage.class)
                    .isEqualTo(expectedErrorMessage);
    }

    public Object[] endpointsRequiringLocationsInvalidLocationsAndErrorsProduced() {
        ErrorMessage expectedError1 = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("arg0", "may not be null")
                .build();

        ErrorMessage expectedError2 = new ErrorMessage.Builder()
                .detailMessage("Parameter validation failed")
                .type(PARAM_VALIDATION)
                .status(BAD_REQUEST)
                .addParamError("latitude", "must be between -90.0 and 90.0")
                .addParamError("longitude", "must be between -180.0 and 180.0")
                .build();

        return new Object[][] {
                { "v1/waypoints/near", "", expectedError1 },
                { "v1/waypoints/near", Coordinates.ofLatLng(1111d, 12313d).toString(), expectedError2 },
                { "v1/waypoints/pois/near", "", expectedError1 },
                { "v1/waypoints/pois/near", Coordinates.ofLatLng(1111d, 12313d).toString(), expectedError2 },
                { "v1/waypoints/walkways/near", "", expectedError1 },
                { "v1/waypoints/walkways/near", Coordinates.ofLatLng(1111d, 12313d).toString(), expectedError2 },
        };
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
                .method("PATCH", entity("[]", APPLICATION_JSON_PATCH_TYPE));

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(NOT_FOUND)
                .extractingEntityAs(ErrorMessage.class)
                    .extracting(ErrorMessage::getErrorType, ErrorMessage::getStatusCode, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
                    .containsOnly(ErrorMessage.Type.NOT_FOUND, NOT_FOUND.getStatusCode(), "Resource not found", null);

        verify(getMockWaypointService(), never()).persist(any());
    }
}
