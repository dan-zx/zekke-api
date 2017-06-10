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

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.service.WaypointService;
import com.github.danzx.zekke.test.spring.BaseSpringValidationTest;
import com.github.danzx.zekke.transformer.Transformer;
import com.github.danzx.zekke.ws.rest.config.ModelTransformerConfig;
import com.github.danzx.zekke.ws.rest.config.ObjectMapperConfig;
import com.github.danzx.zekke.ws.rest.config.WaypointToPoiMapping;
import com.github.danzx.zekke.ws.rest.config.WaypointToTypedWaypointMapping;
import com.github.danzx.zekke.ws.rest.config.WaypointToWalkwayMapping;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;
import com.github.danzx.zekke.ws.rest.patch.ObjectPatch;
import com.github.danzx.zekke.ws.rest.patch.jsonpatch.JsonObjectPatch;

import com.github.fge.jsonpatch.JsonPatch;

import net.rakugakibox.spring.boot.orika.OrikaAutoConfiguration;

import org.junit.Before;
import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        OrikaAutoConfiguration.class,
        ModelTransformerConfig.class,
        WaypointToPoiMapping.class,
        WaypointToTypedWaypointMapping.class,
        WaypointToWalkwayMapping.class,
        ObjectMapperConfig.class
})
public class WaypointEndpointTest extends BaseSpringValidationTest {

    private @Inject Transformer<Waypoint, Poi> waypointToPoiTransformer;
    private @Inject Transformer<Waypoint, TypedWaypoint> waypointToWalkwayTransformer;
    private @Inject Transformer<Waypoint, Walkway> waypointToTypedWaypointTransformer;
    private @Inject ObjectMapper mapper;

    private WaypointService mockWaypointService;
    private WaypointEndpoint endpoint;

    @Before
    public void setUp() {
        assertThat(waypointToPoiTransformer).isNotNull();
        assertThat(waypointToWalkwayTransformer).isNotNull();
        assertThat(waypointToTypedWaypointTransformer).isNotNull();
        assertThat(mapper).isNotNull();
        mockWaypointService = mock(WaypointService.class);
        endpoint = new WaypointEndpoint(mockWaypointService, waypointToPoiTransformer, waypointToTypedWaypointTransformer, waypointToWalkwayTransformer);
    }

    @Test
    public void shouldGetTypedWaypointsFailValidationWhenBboxNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getTypedWaypoints", BoundingBox.class);
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
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
    public void shouldGetWalkwaysFailValidationWhenBboxNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getWalkways", BoundingBox.class);
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
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
    public void shouldGetPoisFailValidationWhenBboxIsNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getPois", BoundingBox.class, String.class);
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
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
        Method method = WaypointEndpoint.class.getMethod("newWaypoint", TypedWaypoint.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadRequiredFieldsAreNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("newWaypoint", TypedWaypoint.class);
        Object[] parameterValues = { new TypedWaypoint() };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldNewWaypointFailValidationWhenIdIsNotNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("newWaypoint", TypedWaypoint.class);
        TypedWaypoint payload = new TypedWaypoint();
        payload.setId(1L);
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(12.43, 43.5));
        Object[] parameterValues = { payload };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldNewWaypointFailValidationWhenPayloadLocationIsNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("newWaypoint", TypedWaypoint.class);
        TypedWaypoint payload = new TypedWaypoint();
        payload.setName("A Name");
        payload.setType(Type.POI);
        payload.setLocation(Coordinates.ofLatLng(1111d, 12313d));
        Object[] parameterValues = { payload };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldGetPoiSuggestionsFailValidationWhenBboxIsNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getPoiSuggestions", BoundingBox.class, String.class);
        BoundingBox bbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(1111d, 12313d), Coordinates.ofLatLng(1111d, 12313d));
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
    public void shouldGetNearTypedWaypointsFailValidationWhenCoordiantesIsNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearTypedWaypoints", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { Coordinates.ofLatLng(1111d, 12313d), null, null };
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
    public void shouldGetNearPoisFailValidationWhenCoordiantesIsNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearPois", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { Coordinates.ofLatLng(1111d, 12313d), null, null };
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
    public void shouldGetNearWalwaysFailValidationWhenCoordiantesIsNotNullButInvalid() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("getNearWalways", Coordinates.class, Integer.class, Integer.class);
        Object[] parameterValues = { Coordinates.ofLatLng(1111d, 12313d), null, null };
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

    @Test
    public void shouldPatchWaypointFailValidationWhenIdAndOrObjectPatchIsNull() throws Exception {
        Method method = WaypointEndpoint.class.getMethod("patchWaypoint", Long.class, ObjectPatch.class);
        Object[] parameterValues = { null, null };
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void shouldPatchWaypointRespondWithNotFoundWhenNoDataIsFound() throws Exception {
        when(mockWaypointService.findWaypointById(anyLong())).thenReturn(Optional.empty());

        Response response = endpoint.patchWaypoint(1L, dummyPatch());

        verify(mockWaypointService, never()).persist(any());

        assertThat(response).isNotNull()
            .extracting(Response::getStatusInfo, Response::hasEntity, Response::getEntity)
            .containsOnly(Status.NOT_FOUND, false, null);
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

        when(mockWaypointService.findWaypointById(id)).thenReturn(Optional.of(waypoint));

        Response response = endpoint.patchWaypoint(id, dummyPatch());

        verify(mockWaypointService).persist(waypoint);

        assertThat(response).isNotNull()
            .extracting(Response::getStatusInfo, Response::hasEntity)
            .containsOnly(Status.OK, true);

        TypedWaypoint actualResponseBody = (TypedWaypoint) response.getEntity();
        assertThat(actualResponseBody).isNotNull()
            .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getLocation, TypedWaypoint::getType)
            .containsOnly(responseBody.getId(), responseBody.getName(), responseBody.getLocation(), responseBody.getType());
    }

    private ObjectPatch dummyPatch() throws Exception {
        JsonPatch jsonPatch = mapper.readValue("[]", JsonPatch.class);
        return new JsonObjectPatch(mapper, jsonPatch);
    }

    private void assertBoundingBoxValidation(Method method, Object[] parameterValues) {
        Set<ConstraintViolation<WaypointEndpoint>> violations = validator().forExecutables().validateParameters(
                endpoint,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(4);
    }
}
