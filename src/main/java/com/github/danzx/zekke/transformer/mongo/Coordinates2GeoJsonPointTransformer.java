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

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.transformer.Transformer;

import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Point;

import org.springframework.stereotype.Component;

/**
 * Transforms domain Coordinates to GeoJson Point a viceversa.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class Coordinates2GeoJsonPointTransformer implements Transformer<Coordinates, Point> {

    @Override
    public Point convertAtoB(Coordinates source) {
        if (source == null) return null;
        return GeoJson.point(source.getLatitude(), source.getLongitude());
    }

    @Override
    public Coordinates convertBtoA(Point source) {
        if (source == null) return null;
        return Coordinates.ofLatLng(source.getLatitude(), source.getLongitude());
    }
}
