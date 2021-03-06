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
package com.github.danzx.zekke.domain.transformer.mongo;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.transformer.Transformer;

import org.mongodb.morphia.query.Shape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * Transforms BoundingBox objects to GeoJson Shapes and viceversa.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class BoundingBox2GeoJsonShapeTransformer implements Transformer<BoundingBox, Shape> {

    private static final Logger log = LoggerFactory.getLogger(BoundingBox2GeoJsonShapeTransformer.class);

    @Override
    public Shape convertAtoB(BoundingBox source) {
        log.debug("bbox: {}", source);
        if (source == null) return null;
        return Shape.box(
                new Shape.Point(source.getBottomCoordinates().getLongitude(), source.getBottomCoordinates().getLatitude()), 
                new Shape.Point(source.getTopCoordinates().getLongitude(), source.getTopCoordinates().getLatitude()));
    }

    @Override
    public BoundingBox convertBtoA(Shape source) {
        log.debug("Shape: {}", source);
        throw new UnsupportedOperationException();
    }
}
