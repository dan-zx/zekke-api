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
package com.github.danzx.zekke.ws.rest.v1;

import static java.util.Collections.emptyList;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.service.WaypointService;
import com.github.danzx.zekke.test.BaseValidationTest;
import com.github.danzx.zekke.ws.rest.common.Poi;
import com.github.danzx.zekke.ws.rest.common.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.common.Walkway;
import com.github.danzx.zekke.ws.rest.transformer.impl.Waypoint2PoiTransformer;
import com.github.danzx.zekke.ws.rest.transformer.impl.Waypoint2TypedWaypointTransformer;
import com.github.danzx.zekke.ws.rest.transformer.impl.Waypoint2WalkwayTransformer;

import org.junit.Before;
import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class WaypointEndpointTest extends BaseValidationTest {

    private WaypointService mockWaypointService;
    private WaypointEndpoint endpoint;

    @Before
    public void setUp() {
        mockWaypointService = mock(WaypointService.class);
        endpoint = new WaypointEndpoint(mockWaypointService, 
                new Waypoint2PoiTransformer(), 
                new Waypoint2WalkwayTransformer(),
                new Waypoint2TypedWaypointTransformer());
    }

    @Test
    public void shouldGetTypedWaypointsFailValidationWhenBboxIsPresentAndInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getTypedWaypoints", BoundingBox.class);
        BoundingBox bbox = new BoundingBox(Coordinates.ofLatLng(1111, 12313), Coordinates.ofLatLng(1111, 12313));
        Object[] parameterValues = { bbox };

        assertBoundingBoxValidation(method, parameterValues);
    }

    @Test
    public void shouldGetTypedWaypointsWontFail() {
        when(mockWaypointService.findWaypoints(any())).thenReturn(emptyList());
        List<TypedWaypoint> result = endpoint.getTypedWaypoints(null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetWalkwaysFailValidationWhenBboxIsPresentAndInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getWalkways", BoundingBox.class);
        BoundingBox bbox = new BoundingBox(Coordinates.ofLatLng(1111, 12313), Coordinates.ofLatLng(1111, 12313));
        Object[] parameterValues = { bbox };

        assertBoundingBoxValidation(method, parameterValues);
    }

    @Test
    public void shouldGetWalkwayWontFail() {
        when(mockWaypointService.findWaypoints(any())).thenReturn(emptyList());
        List<Walkway> result = endpoint.getWalkways(null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetPoisFailValidationWhenBboxIsPresentAndInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getPois", BoundingBox.class, String.class);
        BoundingBox bbox = new BoundingBox(Coordinates.ofLatLng(1111, 12313), Coordinates.ofLatLng(1111, 12313));
        Object[] parameterValues = { bbox, null };

        assertBoundingBoxValidation(method, parameterValues);
    }

    @Test
    public void shouldGetPoisWontFail() {
        when(mockWaypointService.findWaypoints(any())).thenReturn(emptyList());
        List<Poi> result = endpoint.getPois(null, null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldNewWaypointWontFail() {
        final long newId = 12L;
        TypedWaypoint payload = new TypedWaypoint();
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Waypoint waypoint = invocation.getArgumentAt(0, Waypoint.class);
                waypoint.setId(newId);
                return null;
            }
        }).when(mockWaypointService).persist(any());
        TypedWaypoint response = endpoint.newWaypoint(payload);

        assertThat(response).isNotNull()
            .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
            .containsOnly(newId, payload.getName(), payload.getLocation(), payload.getType());
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("newWaypoint", TypedWaypoint.class );
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadFieldsFailValidation() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("newWaypoint", TypedWaypoint.class );
        Object[] parameterValues = { new TypedWaypoint() };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldGetPoiSuggestionsFailValidationWhenBboxIsPresentAndInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getPoiSuggestions", BoundingBox.class, String.class);
        BoundingBox bbox = new BoundingBox(Coordinates.ofLatLng(1111, 12313), Coordinates.ofLatLng(1111, 12313));
        Object[] parameterValues = { bbox, null };

        assertBoundingBoxValidation(method, parameterValues);
    }

    @Test
    public void shouldGetPoiSuggestionsWontFail() {
        when(mockWaypointService.findPoisForNameCompletion(any(), any())).thenReturn(emptyList());
        List<Poi> result = endpoint.getPoiSuggestions(null, null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetNearTypedWaypointsFailValidationWhenCoordiantesIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearTypedWaypoints", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { null, null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldGetNearTypedWaypointsFailValidationWhenCoordiantesFailValidation() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearTypedWaypoints", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { Coordinates.ofLatLng(1111, 12313), null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldGetNearTypedWaypointsWontFail() {
        when(mockWaypointService.findNearWaypoints(any())).thenReturn(emptyList());
        List<TypedWaypoint> result = endpoint.getNearTypedWaypoints(Coordinates.ofLatLng(12.43, 43.5), null, null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetNearPoisFailValidationWhenCoordiantesIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearPois", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { null, null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldGetNearPoisFailValidationWhenCoordiantesFailValidation() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearPois", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { Coordinates.ofLatLng(1111, 12313), null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldGetNearPoisWontFail() {
        when(mockWaypointService.findNearWaypoints(any())).thenReturn(emptyList());
        List<Poi> result = endpoint.getNearPois(Coordinates.ofLatLng(12.43, 43.5), null, null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetNearWalwaysFailValidationWhenCoordiantesIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearWalways", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { null, null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldGetNearWalwaysFailValidationWhenCoordiantesFailValidation() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearWalways", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { Coordinates.ofLatLng(1111, 12313), null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldGetNearWalkwaysWontFail() {
        when(mockWaypointService.findNearWaypoints(any())).thenReturn(emptyList());
        List<Walkway> result = endpoint.getNearWalways(Coordinates.ofLatLng(12.43, 43.5), null, null);

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldGetWaypointFailValidationWhenIdIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getWaypoint", Long.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
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
        
        when(mockWaypointService.findWaypointById(responseBody.getId())).thenReturn(Optional.of(waypoint));
        Response response = endpoint.getWaypoint(responseBody.getId());

        assertThat(response).isNotNull()
            .extracting(Response::getStatusInfo, Response::hasEntity)
            .containsOnly(Status.OK, true);
        TypedWaypoint actualResponseBody = (TypedWaypoint) response.getEntity();
        assertThat(actualResponseBody).isNotNull()
            .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
            .containsOnly(responseBody.getId(), responseBody.getName(), responseBody.getLocation(), responseBody.getType());
    }

    @Test
    public void shouldGetWaypointRespondWithNotFoundWhenNoData() {
        when(mockWaypointService.findWaypointById(5L)).thenReturn(Optional.empty());
        Response response = endpoint.getWaypoint(5L);

        assertThat(response).isNotNull()
            .extracting(Response::getStatusInfo, Response::hasEntity, Response::getEntity)
            .containsOnly(Status.NOT_FOUND, false, null);
    }

    @Test
    public void shouldDeleteWaypointFailValidationWhenIdIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("deleteWaypoint", Long.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldDeleteWaypointRespondWithNoContent() {
        when(mockWaypointService.delete(any())).thenReturn(true);
        Response response = endpoint.deleteWaypoint(5L);

        assertThat(response).isNotNull()
            .extracting(Response::getStatusInfo, Response::hasEntity, Response::getEntity)
            .containsOnly(Status.NO_CONTENT, false, null);
    }

    @Test
    public void shouldDeleteWaypointRespondWithNotFoundWhenNoData() {
        when(mockWaypointService.delete(any())).thenReturn(false);
        Response response = endpoint.deleteWaypoint(5L);

        assertThat(response).isNotNull()
            .extracting(Response::getStatusInfo, Response::hasEntity, Response::getEntity)
            .containsOnly(Status.NOT_FOUND, false, null);
    }

    private void assertBoundingBoxValidation(Method method, Object[] parameterValues) {
//        BoundingBox bbox = new BoundingBox(Coordinates.ofLatLng(1111, 12313), Coordinates.ofLatLng(1111, 12313));
//        Object[] parameterValues = { bbox };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(4);
    }
}
