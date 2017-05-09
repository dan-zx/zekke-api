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

import com.github.danzx.zekke.domain.Coordinates;

/**
 * Transforms Coordinates string parameter into the object and viceversa.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class CoordinatesConverterProvider implements ParamConverterProvider {

    @Override
    @SuppressWarnings("unchecked") // Nonsense cast
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType == Coordinates.class) return (ParamConverter<T>) new Converter(); 
        return null;
    }

    public static class Converter implements ParamConverter<Coordinates> {

        private static final String LAT_LNG_SEPARATOR = ",";

        @Override
        public Coordinates fromString(String value) {
            if (value == null) return null;
            String[] latlng = value.split(LAT_LNG_SEPARATOR);
            if (latlng.length != 2) throw new IllegalArgumentException("Coordinates bad format");
            try {
                double latitude = Double.parseDouble(latlng[0]);
                double longitude = Double.parseDouble(latlng[1]);
                return Coordinates.ofLatLng(latitude, longitude);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Componets of Coordinates must be doubles");
            }
        }

        @Override
        public String toString(Coordinates value) {
            if (value == null) return null;
            return value.getLatitude() + LAT_LNG_SEPARATOR + value.getLongitude();
        }
    }
}
