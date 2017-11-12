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
package com.github.danzx.zekke.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.github.danzx.zekke.data.filter.waypoint.LocationWaypointFilterOptions;
import com.github.danzx.zekke.data.filter.waypoint.WaypointFilterOptions;
import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.test.mockito.BaseMockitoValidationTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@RunWith(JUnitParamsRunner.class)
public class WaypointServiceImplTest extends BaseMockitoValidationTest {

    private @Mock WaypointDao dao;
    
    private @InjectMocks WaypointServiceImpl service;

    @Test
    public void shouldFailValidationWhenPersistNull() throws Exception {
        Method method = WaypointServiceImpl.class.getMethod("persist", Waypoint.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointServiceImpl>> violations = validator().forExecutables().validateParameters(
                service,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @Parameters(method = "invalidWaypointsToPersist")
    public void shouldFailValidationWhenPersistInvalidWaypoint(Waypoint waypoint, int numberOfViolations) throws Exception {
        Method method = WaypointServiceImpl.class.getMethod("persist", Waypoint.class);
        Object[] parameterValues = { waypoint };
        Set<ConstraintViolation<WaypointServiceImpl>> violations = validator().forExecutables().validateParameters(
                service,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(numberOfViolations);
    }

    @Test
    public void shouldForwardPersistCall() {
        Waypoint walkway = new Waypoint();
        walkway.setType(Type.WALKWAY);
        walkway.setLocation(Coordinates.ofLatLng(12.24, 53.545));
        service.persist(walkway); 
        verify(dao).saveOrUpdate(walkway); 
    }

    @Test
    @Parameters(method = "invalidWaypointsToDelete")
    public void shouldFailValidationWhenDeleteInvalidWaypoint(Waypoint waypoint) throws Exception {
        Method method = WaypointServiceImpl.class.getMethod("delete", Waypoint.class);
        Object[] parameterValues = { waypoint };
        Set<ConstraintViolation<WaypointServiceImpl>> violations = validator().forExecutables().validateParameters(
                service,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldForwardDeleteCall() {
        long id = 2L;
        Waypoint w = new Waypoint();
        w.setId(id);
        service.delete(w);
        verify(dao).deleteById(id);
    } 

    @Test
    public void shouldForwardFindById() {
        long id = 2L;
        service.findWaypointById(id);
        verify(dao).findById(id);
    }

    @Test
    public void shouldForwardToFindFiltered() {
        Coordinates c1 = Coordinates.ofLatLng(12.24, 53.545);
        Coordinates c2 = Coordinates.ofLatLng(21.45, 37.5);
        BoundingBox bbox = BoundingBox.ofBottomTop(c1, c2);
        WaypointFilterOptions filterOptions = new WaypointFilterOptions.Builder()
            .withinBoundingBox(bbox)
            .byType(Type.POI)
            .limitResulsTo(1)
            .onlyIdAndName()
            .withNameContaining("x")
            .build();
        service.findWaypoints(filterOptions);
        verify(dao).findFiltered(filterOptions);
    }

    @Test
    public void shouldFindWaypointsFailValidationWhenFilterOptionsIsNull() throws Exception {
        Method method = WaypointServiceImpl.class.getMethod("findWaypoints", WaypointFilterOptions.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointServiceImpl>> violations = validator().forExecutables().validateParameters(
                service,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldForwardToFindNearWhenFindNearWaypoints() {
        LocationWaypointFilterOptions filterOptions = LocationWaypointFilterOptions.Builder
                .nearLocation(Coordinates.ofLatLng(12.24, 53.545))
                .byType(Type.WALKWAY)
                .limitResulsTo(4)
                .maximumSearchDistance(1_000)
                .build();
        service.findWaypointsNearALocation(filterOptions);
        verify(dao).findNearALocationFiltered(filterOptions);
    }

    @Test
    public void shouldFindWaypointsNearALocationFailValidationWhenFilterOptionsIsNull() throws Exception {
        Method method = WaypointServiceImpl.class.getMethod("findWaypointsNearALocation", LocationWaypointFilterOptions.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<WaypointServiceImpl>> violations = validator().forExecutables().validateParameters(
                service,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    public Object[] invalidWaypointsToDelete() {
        Waypoint w1 = new Waypoint();
        w1.setType(Type.POI);
        w1.setLocation(Coordinates.ofLatLng(12.4, -52.5));
        w1.setName("Name");

        return new Object[][] {
            {null},
            {new Waypoint()},
            {w1}
        };
    }

    public Object[] invalidWaypointsToPersist() {
        Waypoint w1 = new Waypoint();
        Waypoint w2 = new Waypoint();
        w2.setType(Type.POI);

        Waypoint w3 = new Waypoint();
        w3.setType(Type.WALKWAY);

        Waypoint w4 = new Waypoint();
        w4.setType(Type.WALKWAY);
        w4.setLocation(Coordinates.ofLatLng(12.4, -52.5));
        w4.setName("Name");

        Waypoint w5 = new Waypoint();
        w5.setType(Type.POI);
        w5.setLocation(Coordinates.ofLatLng(12.4, -52.5));

        return new Object[][] {
            {w1, 2},
            {w2, 2},
            {w3, 1},
            {w4, 1},
            {w5, 1}
        };
    }
}
