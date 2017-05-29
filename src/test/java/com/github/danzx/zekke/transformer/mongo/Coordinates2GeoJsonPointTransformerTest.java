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
package com.github.danzx.zekke.transformer.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.danzx.zekke.domain.Coordinates;
import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Point;
import org.junit.Test;

public class Coordinates2GeoJsonPointTransformerTest {

    private static final Coordinates2GeoJsonPointTransformer TRANSFORMER = new Coordinates2GeoJsonPointTransformer();

    @Test
    public void shouldCoordinatesToGeoJsonPoint() {
        Coordinates latlng = Coordinates.ofLatLng(19.054492, -98.283176);
        assertThat(TRANSFORMER.convertAtoB(latlng)).isNotNull().extracting(Point::getLatitude, Point::getLongitude).containsExactly(latlng.getLatitude(), latlng.getLongitude());
    }

    @Test
    public void shouldNullAtoNullB() {
        assertThat(TRANSFORMER.convertAtoB(null)).isNull();
    }

    @Test
    public void shouldGeoJsonPointToCoordinates() {
        Point point = GeoJson.point(19.054492, -98.283176);
        assertThat(TRANSFORMER.convertBtoA(point)).isNotNull().extracting(Coordinates::getLatitude, Coordinates::getLongitude).containsExactly(point.getLatitude(), point.getLongitude());
    }

    @Test
    public void shouldNullBtoNullA() {
        assertThat(TRANSFORMER.convertBtoA(null)).isNull();
    }
}
