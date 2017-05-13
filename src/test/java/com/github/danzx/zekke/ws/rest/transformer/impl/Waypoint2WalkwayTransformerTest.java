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
package com.github.danzx.zekke.ws.rest.transformer.impl;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.ws.rest.common.Walkway;
import com.github.danzx.zekke.ws.rest.transformer.TransformationException;
import org.junit.Test;

public class Waypoint2WalkwayTransformerTest {

    private Waypoint2WalkwayTransformer TRANSFORMER = new Waypoint2WalkwayTransformer();

    @Test
    public void shouldConvertSingle() {
        Waypoint waypoint = newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5);
        Walkway actualWalkway = TRANSFORMER.convert(waypoint);
        assertThat(actualWalkway).isNotNull().extracting(Walkway::getId, Walkway::getLocation).containsOnly(waypoint.getId(), waypoint.getLocation());
    }

    @Test
    public void shouldRevertSingle() {
        Waypoint expectedWaypoint = newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5);
        Waypoint actualWaypoint = TRANSFORMER.revert(TRANSFORMER.convert(expectedWaypoint));
        assertThat(actualWaypoint).isNotNull().isEqualTo(expectedWaypoint);
    }

    @Test
    public void shouldConvertNull() {
        assertThat(TRANSFORMER.convert(null)).isNull();
    }

    @Test
    public void shouldRevertNull() {
        assertThat(TRANSFORMER.revert(null)).isNull();
    }

    @Test
    public void shouldConvertList() {
        Waypoint waypoint = newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5);
        List<Walkway> actualWalkways = TRANSFORMER.convertList(singletonList(waypoint));
        assertThat(actualWalkways).isNotNull().isNotEmpty().extracting(Walkway::getId, Walkway::getLocation).containsOnly(tuple(waypoint.getId(), waypoint.getLocation()));
    }

    @Test
    public void shouldConvertEmptyList() {
        assertThat(TRANSFORMER.convertList(emptyList())).isNotNull().isEmpty();
    }

    @Test
    public void shouldConvertSingleThrowTransformationExceptionWhenWaypointIsNotPoi() {
        Waypoint waypoint = newWaypoint(1L, "Name", Type.POI, 12.43, 43.5);
        assertThatThrownBy(() -> TRANSFORMER.convert(waypoint)).isInstanceOf(TransformationException.class);
    }

    @Test
    public void shouldRevertList() {
        List<Waypoint> expectedWaypoints = singletonList(newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5));
        List<Waypoint> actualWaypoints = TRANSFORMER.revertList(TRANSFORMER.convertList(expectedWaypoints));
        assertThat(actualWaypoints).isNotNull().isNotEmpty().hasSameSizeAs(expectedWaypoints).isEqualTo(expectedWaypoints);
    }

    @Test
    public void shouldRevertEmptyList() {
        assertThat(TRANSFORMER.revertList(emptyList())).isNotNull().isEmpty();
    }

    @Test
    public void shouldConvertListThrowTransformationExceptionWhenWaypointIsNotPoi() {
        Waypoint waypoint = newWaypoint(1L, "Name", Type.POI, 12.43, 43.5);
        assertThatThrownBy(() -> TRANSFORMER.convertList(singletonList(waypoint))).isInstanceOf(TransformationException.class);
    }

    private Waypoint newWaypoint(long id, String name, Type type, double lat, double lng) {
        Waypoint waypoint = new Waypoint();
        waypoint.setId(id);
        if (name != null) waypoint.setName(name);
        waypoint.setType(type);
        waypoint.setLocation(Coordinates.ofLatLng(lat, lng));
        return waypoint;
    }
}