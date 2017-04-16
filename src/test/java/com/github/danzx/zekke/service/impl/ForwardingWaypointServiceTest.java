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

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.persistence.dao.WaypointDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ForwardingWaypointServiceTest {

    private @Mock WaypointDao dao;

    private @InjectMocks ForwardingWaypointService service;

    @Test
    public void shouldForwardPersistCall() {
        Waypoint w = new Waypoint();
        service.persist(w);
        verify(dao).save(w);
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
    public void shouldThrowNullPointerExceptionWhenDeleteANullWaypoint() {
        assertThatThrownBy(() -> service.delete(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldForwardFindNearestCall() {
        Coordinates location = Coordinates.ofLatLng(21.34746, -23.5468);
        int maxDistance = 5;
        service.findNearest(location, maxDistance);
        verify(dao).findNearest(location, maxDistance);
    }

    @Test
    public void shouldForwardFindPoisByNameLikeCall() {
        String name = "xx";
        service.findPoisByNameLike(name);
        verify(dao).findPoisByNameLike(name);
    }

    @Test
    public void shouldForwardFindPoisWithinBoxCall() {
        Coordinates bottomLeftCoordinates = Coordinates.ofLatLng(21.34746, -23.5468);
        Coordinates upperRightCoordinates = Coordinates.ofLatLng(57.34746, -79.5468);
        service.findPoisWithinBox(bottomLeftCoordinates, upperRightCoordinates);
        verify(dao).findPoisWithinBox(bottomLeftCoordinates, upperRightCoordinates);
    }

    @Test
    public void shouldForwardFindNamesWithinBoxLikeCall() {
        String name = "xx";
        Coordinates bottomLeftCoordinates = Coordinates.ofLatLng(21.34746, -23.5468);
        Coordinates upperRightCoordinates = Coordinates.ofLatLng(57.34746, -79.5468);
        service.findNamesWithinBoxLike(name, bottomLeftCoordinates, upperRightCoordinates);
        verify(dao).findNamesWithinBoxLike(name, bottomLeftCoordinates, upperRightCoordinates);
    }
}
