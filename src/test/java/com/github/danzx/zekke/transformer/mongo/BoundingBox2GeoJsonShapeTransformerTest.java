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

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;

import org.junit.Test;

public class BoundingBox2GeoJsonShapeTransformerTest {

    private static final BoundingBox2GeoJsonShapeTransformer TRANSFORMER = new BoundingBox2GeoJsonShapeTransformer();

    @Test
    public void shouldConvertBoundingBoxToGeoJsonShape() {
        BoundingBox bbox = BoundingBox.ofBottomTop(
                Coordinates.ofLatLng(19.054492, -98.283176), 
                Coordinates.ofLatLng(43.9876, -103.7564));

        assertThat(TRANSFORMER.convertAtoB(bbox)).isNotNull();
    }

    @Test
    public void shouldNullToNull() {
        assertThat(TRANSFORMER.convertAtoB(null)).isNull();
    }
}
