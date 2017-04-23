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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.service.ServiceException;
import com.github.danzx.zekke.service.WaypointService.NearWaypointsQuery;
import com.github.danzx.zekke.service.WaypointService.WaypointsQuery;
import com.github.danzx.zekke.test.mockito.BaseMockitoTest;

import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

public class WaypointServiceImplTest extends BaseMockitoTest {

    private @Mock WaypointDao dao; 
    
    private @InjectMocks WaypointServiceImpl service; 

    @Test 
    public void shouldThrowNullPointerExceptionWhenPersistNull() { 
        assertThatThrownBy(() -> service.persist(null)).isInstanceOf(NullPointerException.class); 
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenPersistWaypointWithoutType() {
        Waypoint waypoint = new Waypoint();
        waypoint.setName("a name");
        waypoint.setLocation(Coordinates.ofLatLng(12.24, 53.545));
        assertThatThrownBy(() -> service.persist(new Waypoint())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenPersistWaypointWithoutLocation() {
        Waypoint waypoint = new Waypoint();
        waypoint.setName("a name");
        waypoint.setType(Type.POI);
        assertThatThrownBy(() -> service.persist(new Waypoint())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowServiceExceptionWhenPersistPoiWithoutName() {
        Waypoint poi = new Waypoint();
        poi.setType(Type.POI);
        poi.setLocation(Coordinates.ofLatLng(12.24, 53.545));
        assertThatThrownBy(() -> service.persist(poi)).isInstanceOf(ServiceException.class);
    }

    @Test
    public void shouldThrowServiceExceptionWhenPersistWalkwayWithName() {
        Waypoint walkway = new Waypoint();
        walkway.setType(Type.WALKWAY);
        walkway.setName("a name");
        walkway.setLocation(Coordinates.ofLatLng(12.24, 53.545));
        assertThatThrownBy(() -> service.persist(walkway)).isInstanceOf(ServiceException.class);
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
    public void shouldForwardPersistCall2() { 
        Waypoint poi = new Waypoint();
        poi.setType(Type.POI);
        poi.setName("Name");
        poi.setLocation(Coordinates.ofLatLng(12.24, 53.545));
        service.persist(poi); 
        verify(dao).saveOrUpdate(poi); 
    } 
 
    @Test 
    public void shouldThrowNullPointerExceptionWhenDeleteNull() { 
        assertThatThrownBy(() -> service.delete(null)).isInstanceOf(NullPointerException.class); 
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
    public void shouldForwardToFindWithinBoxWhenFindWaypointsHasBbox() {
        WaypointsQuery query = new WaypointsQuery.Builder()
                .withinBoundingBox(Coordinates.ofLatLng(12.24, 53.545), Coordinates.ofLatLng(21.45, 37.5))
                .build();
        service.findWaypoints(query);
        verify(dao).findWithinBox(query.getBoundingBox().get()[0], query.getBoundingBox().get()[1], query.getWaypointType(), query.getNameQuery(), false);
    }

    @Test 
    public void shouldForwardToFindOptionallyByTypeAndNameQueryWhenFindWaypointsHasNoBbox() {
        WaypointsQuery query = new WaypointsQuery.Builder().build();
        service.findWaypoints(query);
        verify(dao).findOptionallyByTypeAndNameQuery(query.getWaypointType(), query.getNameQuery());
    }

    @Test 
    public void shouldForwardToFindNearWhenFindNearWaypoints() {
        NearWaypointsQuery query = new NearWaypointsQuery.Builder(Coordinates.ofLatLng(12.24, 53.545)).build();
        service.findNearWaypoints(query);
        verify(dao).findNear(query.getLocation(), query.getMaxDistance(), query.getLimit(), query.getWaypointType());
    }

    @Test 
    public void shouldForwardToFindWithinBoxWhenFindPoisForNameCompletion() {
        Coordinates c1 = Coordinates.ofLatLng(12.24, 53.545);
        Coordinates c2 = Coordinates.ofLatLng(21.45, 37.5);
        service.findPoisForNameCompletion(c1, c2, Optional.empty());
        verify(dao).findWithinBox(c1, c2, Optional.of(Type.POI), Optional.empty(), true);
    }
}
