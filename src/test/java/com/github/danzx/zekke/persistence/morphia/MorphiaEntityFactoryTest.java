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
package com.github.danzx.zekke.persistence.morphia;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.danzx.zekke.persistence.morphia.MorphiaWaypoint.Type;

public class MorphiaEntityFactoryTest {

    private static final MorphiaEntityFactory FACTORY = new MorphiaEntityFactory();

    @Test
    public void shouldCreatePoi() {
        MorphiaWaypoint waypoint = FACTORY.newPoi();
        assertThat(waypoint).isNotNull();
        assertThat(waypoint.getType()).isNotNull().isEqualTo(Type.POI);
    }

    @Test
    public void shouldCreateWalkway() {
        MorphiaWaypoint waypoint = FACTORY.newWalkway();
        assertThat(waypoint).isNotNull();
        assertThat(waypoint.getType()).isNotNull().isEqualTo(Type.WALKWAY);
    }

    @Test
    public void shouldCreatePath() {
        MorphiaPath path = FACTORY.newPath();
        assertThat(path).isNotNull();
    }
}
