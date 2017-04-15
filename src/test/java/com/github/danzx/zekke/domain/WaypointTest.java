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
package com.github.danzx.zekke.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.danzx.zekke.domain.Waypoint.Type;

import org.apache.commons.beanutils.BeanUtils;

import org.junit.Test;

public class WaypointTest {

    private static final Waypoint TEST_WAYPOINT = newTestWaypoint();

    @Test
    public void shouldPathBeNotNullEvenIfSetToNull() {
        Waypoint waypoint = new Waypoint();
        assertThat(waypoint.getPaths()).isNotNull();
        waypoint.setPaths(null);
        assertThat(waypoint.getPaths()).isNotNull();
    }

    @Test
    public void shouldNameBeNotNullEvenIfSetToNull() {
        Waypoint waypoint = new Waypoint();
        assertThat(waypoint.getName()).isNotNull();
        waypoint.setName(null);
        assertThat(waypoint.getName()).isNotNull();
    }

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(TEST_WAYPOINT.equals(TEST_WAYPOINT)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        assertThat(TEST_WAYPOINT.equals(newTestWaypoint())).isTrue();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(TEST_WAYPOINT.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(TEST_WAYPOINT.equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        Waypoint waypoint2 = newTestWaypoint();
        waypoint2.setId(2L);
        assertThat(TEST_WAYPOINT.equals(waypoint2)).isFalse();

        copy(TEST_WAYPOINT, waypoint2);
        waypoint2.setType(Type.WALKWAY);
        assertThat(TEST_WAYPOINT.equals(waypoint2)).isFalse();

        copy(TEST_WAYPOINT, waypoint2);
        waypoint2.setLocation(Coordinates.ofLatLng(21.4235601, -101.546821));
        assertThat(TEST_WAYPOINT.equals(waypoint2)).isFalse();

        copy(TEST_WAYPOINT, waypoint2);
        waypoint2.setName("Other Name");
        assertThat(TEST_WAYPOINT.equals(waypoint2)).isFalse();
        
        copy(TEST_WAYPOINT, waypoint2);
        waypoint2.setName(null);
        assertThat(TEST_WAYPOINT.equals(waypoint2)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        Waypoint waypoint2 = newTestWaypoint();
        assertThat(TEST_WAYPOINT.hashCode()).isEqualTo(TEST_WAYPOINT.hashCode()).isEqualTo(waypoint2.hashCode());
    }

    private static Waypoint newTestWaypoint() {
        Waypoint testWaypoint = new Waypoint();
        testWaypoint.setId(1L);
        testWaypoint.setLocation(Coordinates.ofLatLng(19.054492, -98.283176));
        testWaypoint.setName("waypoint_1");
        testWaypoint.setType(Type.POI);
        return testWaypoint;
    }

    private void copy(Waypoint src, Waypoint dest) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
