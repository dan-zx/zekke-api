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

import java.util.Arrays;
import java.util.Objects;

import com.github.danzx.zekke.domain.Coordinates;

/**
 * Represents a geometric point.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Point extends GeoJson {

    private static final int COORDINATES_ARRAY_SIZE = 2;
    private static final int LONGITUDE_INDEX = 0;
    private static final int LATITUDE_INDEX = 1;

    private Double[] coordinates;

    public Point() {
        super(GeometryType.POINT);
    }

    /** Creates a new Point with the values of the given Coordinates object. */
    public static Point of(Coordinates coordinates) {
        Point point = new Point();
        point.setLongitude(coordinates.getLongitude());
        point.setLatitude(coordinates.getLatitude());
        return point;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public Double getLongitude() {
        return coordinates == null ? null : coordinates[LONGITUDE_INDEX];
    }

    public void setLongitude(double latitude) {
        initCoordinatesIfNecessary();
        coordinates[LONGITUDE_INDEX] = latitude;
    }

    public Double getLatitude() {
        return coordinates == null ? null : coordinates[LATITUDE_INDEX];
    }

    public void setLatitude(double latitude) {
        initCoordinatesIfNecessary();
        coordinates[LATITUDE_INDEX] = latitude;
    }

    private void initCoordinatesIfNecessary() {
        if (coordinates == null) coordinates = new Double[COORDINATES_ARRAY_SIZE];
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof Point)) return false;
        Point other = (Point) obj;
        return Arrays.equals(coordinates, other.coordinates);
    }

    @Override
    public String toString() {
        return "{ type:\"" + getType() + "\", coordinates: " + Arrays.toString(coordinates) + " }";
    }
}
