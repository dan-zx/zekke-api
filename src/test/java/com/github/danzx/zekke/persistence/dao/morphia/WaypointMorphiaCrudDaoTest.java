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
package com.github.danzx.zekke.persistence.dao.morphia;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

import com.github.danzx.zekke.domain.Path;
import com.github.danzx.zekke.domain.Point;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.mongo.BaseSpringMongoTest;

public class WaypointMorphiaCrudDaoTest extends BaseSpringMongoTest {

    private static final long CDMX_ID = 1L;
    private static final long NOT_EXITING_WAYPOINT = Long.MAX_VALUE;

    @Inject private WaypointMorphiaCrudDao waypointDao;

    @Test
    public void shouldReturnPoiWhenFindByIdHasValidId() {
        Waypoint cdmx = new Waypoint();
        cdmx.setId(CDMX_ID);
        cdmx.setName("Ciudad de México");
        cdmx.setType(Type.POI);
        cdmx.setLocation(new Point());
        cdmx.getLocation().setLatitude(19.387591);
        cdmx.getLocation().setLongitude(-99.052734);
        Path path = new Path();
        path.setFromWaypoint(CDMX_ID);
        path.setToWaypoint(7L);
        path.setDistance(94431.859);

        Optional<Waypoint> waypoint = waypointDao.findById(CDMX_ID);
        assertThat(waypoint.isPresent()).isTrue();
        assertThat(waypoint.get()).isEqualTo(cdmx);
        assertThat(waypoint.get().getPaths()).isNotNull().isNotEmpty().hasSize(1).containsExactly(path);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenFindByIdHasInvalidId() {
        Optional<Waypoint> waypoint = waypointDao.findById(NOT_EXITING_WAYPOINT);
        assertThat(waypoint.isPresent()).isFalse();
    }
}
