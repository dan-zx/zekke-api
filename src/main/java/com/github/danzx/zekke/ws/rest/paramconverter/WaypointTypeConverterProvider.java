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

import com.github.danzx.zekke.domain.Waypoint;

/**
 * Transforms WaypointType string parameter into the object and viceversa.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class WaypointTypeConverterProvider implements ParamConverterProvider {

    @Override
    @SuppressWarnings("unchecked") // Nonsense cast
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType == Waypoint.Type.class) return (ParamConverter<T>) new Converter();
        return null;
    }

    public static class Converter implements ParamConverter<Waypoint.Type> {

        @Override
        public Waypoint.Type fromString(String value) {
            if (value == null) return null;
            return Waypoint.Type.fromPluralName(value, true);
        }

        @Override
        public String toString(Waypoint.Type value) {
            if (value == null) return null;
            return value.pluralName();
        }
    }
}
