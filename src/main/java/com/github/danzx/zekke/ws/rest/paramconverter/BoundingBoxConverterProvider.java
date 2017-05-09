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
package com.github.danzx.zekke.ws.rest.paramconverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;

/**
 * Transforms BoundingBox string parameter into the object and viceversa.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class BoundingBoxConverterProvider implements ParamConverterProvider {

    @Override
    @SuppressWarnings("unchecked") // Nonsense cast
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType == BoundingBox.class) return (ParamConverter<T>) new Converter(); 
        return null;
    }

    public static class Converter implements ParamConverter<BoundingBox> {

        private static final String COORDINATES_SEPARATOR = ";";
        private static final CoordinatesConverterProvider.Converter COORDINATES_CONVERTER = new CoordinatesConverterProvider.Converter();

        @Override
        public BoundingBox fromString(String value) {
            if (value == null) return null;
            String[] bottomLeftAndUpperRigth = value.split(COORDINATES_SEPARATOR);
            if (bottomLeftAndUpperRigth.length != 2) throw new IllegalArgumentException("BoundingBox bad format");
            Coordinates bottomLeftCoordinates = COORDINATES_CONVERTER.fromString(bottomLeftAndUpperRigth[0]);
            Coordinates upperRightCoordinates = COORDINATES_CONVERTER.fromString(bottomLeftAndUpperRigth[1]);
            return new BoundingBox(bottomLeftCoordinates, upperRightCoordinates);
        }

        @Override
        public String toString(BoundingBox value) {
            if (value == null) return null;
            return COORDINATES_CONVERTER.toString(value.getBottomLeftCoordinates()) + COORDINATES_SEPARATOR + COORDINATES_CONVERTER.toString(value.getUpperRightCoordinates()); 
        }
    }
}
