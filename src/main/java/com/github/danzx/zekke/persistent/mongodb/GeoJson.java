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
package com.github.danzx.zekke.persistent.mongodb;

import static com.github.danzx.zekke.util.Strings.allCapsToPascalCase;

import java.util.Objects;

/**
 * GeoJSON is a format for encoding a variety of geographic data structures. A GeoJSON object may
 * represent a geometry, a feature, or a collection of features. 
 * 
 * This class is the base representation of GeoJSON objects.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class GeoJson {

    /** Supported GeoJSON geometry types. */
    public enum GeometryType {
        POINT,
        LINE_STRING,
        POLYGON,
        MULTI_POINT,
        MILTI_LINE_STRING,
        MULTI_POYGON,
        GEOMETRY_COLLECTION;

        private final String fieldValue;

        GeometryType() {
            fieldValue = allCapsToPascalCase(name());
        }

        public String getFieldValue() {
            return fieldValue;
        }
    }

    private final String type;

    protected GeoJson(GeometryType geometryType) {
        type = geometryType.fieldValue;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GeoJson)) return false;
        GeoJson other = (GeoJson) obj;
        return Objects.equals(type, other.type);

    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
