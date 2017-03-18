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
package com.github.danzx.zekke.domain;

import java.util.Arrays;

import com.github.danzx.zekke.constraint.FloatRange;

/**
 * Represents a geometric point.
 * 
 * @see <a href="http://geojson.org/geojson-spec.html">The GeoJSON Format Specification</a>
 * @author Daniel Pedraza-Arcega
 */
public class Point {

    private static final String POINT_TYPE = "Point";
    private static final int COORDINATES_ARRAY_SIZE = 2;
    private static final int LONGITUDE_INDEX = 0;
    private static final int LATITUDE_INDEX = 1;

    public static final double MAX_LATITUDE = 90;
    public static final double MIN_LATITUDE = -MAX_LATITUDE;
    public static final double MAX_LONGITUDE = 180;
    public static final double MIN_LONGITUDE = -MAX_LONGITUDE;

    private final String type;
    private Double[] coordinates;

    public Point() {
        type = POINT_TYPE;
    }

    public String getType() {
        return type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public Double getLongitude() {
        return coordinates == null ? null : coordinates[LONGITUDE_INDEX];
    }

    public void setLongitude(@FloatRange(min = MIN_LONGITUDE, max = MAX_LONGITUDE) Double latitude) {
        initCoordinatesIfNecessary();
        coordinates[LONGITUDE_INDEX] = latitude;
    }

    public Double getLatitude() {
        return coordinates == null ? null : coordinates[LATITUDE_INDEX];
    }

    public void setLatitude(@FloatRange(min = MIN_LATITUDE, max = MAX_LATITUDE) Double latitude) {
        initCoordinatesIfNecessary();
        coordinates[LATITUDE_INDEX] = latitude;
    }

    private void initCoordinatesIfNecessary() {
        if (coordinates == null) coordinates = new Double[COORDINATES_ARRAY_SIZE];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point)) return false;
        Point other = (Point) obj;
        return Arrays.equals(coordinates, other.coordinates);
    }

    @Override
    public String toString() {
        return "{ type:\"" + getType() + "\", coordinates: " + Arrays.toString(coordinates) + " }";
    }
}
