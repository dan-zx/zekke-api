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
package com.github.danzx.zekke.ws.rest.transformer;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;

import javax.inject.Inject;

import com.github.danzx.zekke.config.TransformerConfig;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.spring.BaseSpringTest;
import com.github.danzx.zekke.ws.rest.common.Walkway;

import org.junit.Before;
import org.junit.Test;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TransformerConfig.class)
public class Waypoint2WalkwayTransformerTest extends BaseSpringTest {

    private @Inject Transformer<Waypoint, Walkway> transformer;

    @Before
    public void setUp() {
        assertThat(transformer).isNotNull();
    }

    @Test
    public void shouldConvertSingle() {
        Waypoint waypoint = newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5);
        Walkway actualWalkway = transformer.convert(waypoint);
        assertThat(actualWalkway).isNotNull().extracting(Walkway::getId, Walkway::getLocation).containsOnly(waypoint.getId(), waypoint.getLocation());
    }

    @Test
    public void shouldRevertSingle() {
        Waypoint expectedWaypoint = newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5);
        Waypoint actualWaypoint = transformer.revert(transformer.convert(expectedWaypoint));
        assertThat(actualWaypoint).isNotNull().isEqualTo(expectedWaypoint);
    }

    @Test
    public void shouldConvertNull() {
        assertThat(transformer.convert(null)).isNull();
    }

    @Test
    public void shouldRevertNull() {
        assertThat(transformer.revert(null)).isNull();
    }

    @Test
    public void shouldConvertList() {
        Waypoint waypoint = newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5);
        List<Walkway> actualWalkways = transformer.convertList(singletonList(waypoint));
        assertThat(actualWalkways).isNotNull().isNotEmpty().extracting(Walkway::getId, Walkway::getLocation).containsOnly(tuple(waypoint.getId(), waypoint.getLocation()));
    }

    @Test
    public void shouldConvertEmptyList() {
        assertThat(transformer.convertList(emptyList())).isNotNull().isEmpty();
    }

    @Test
    public void shouldRevertList() {
        List<Waypoint> expectedWaypoints = singletonList(newWaypoint(1L, null, Type.WALKWAY, 12.43, 43.5));
        List<Waypoint> actualWaypoints = transformer.revertList(transformer.convertList(expectedWaypoints));
        assertThat(actualWaypoints).isNotNull().isNotEmpty().hasSameSizeAs(expectedWaypoints).isEqualTo(expectedWaypoints);
    }

    @Test
    public void shouldRevertEmptyList() {
        assertThat(transformer.revertList(emptyList())).isNotNull().isEmpty();
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
