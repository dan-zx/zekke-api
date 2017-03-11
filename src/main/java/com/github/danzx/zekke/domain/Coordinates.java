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

import java.util.Objects;

import com.github.danzx.zekke.constraint.FloatRange;

/**
 * Represents the geographic location of a place in a map.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Coordinates {

    public static final double MAX_LATITUDE = 90;
    public static final double MIN_LATITUDE = -MAX_LATITUDE;
    public static final double MAX_LONGITUDE = 180;
    public static final double MIN_LONGITUDE = -MAX_LONGITUDE;

    @FloatRange(min = MIN_LATITUDE, max = MAX_LATITUDE)
    private final double latitude;

    @FloatRange(min = MIN_LONGITUDE, max = MAX_LONGITUDE)
    private final double longitude;

    private Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor.
     *
     * @param latitude a double between {@value #MIN_LATITUDE} and {@value #MAX_LATITUDE}.
     * @param longitude a double between {@value #MIN_LONGITUDE} and {@value #MAX_LONGITUDE}.
     */
    public static Coordinates ofLatLng(double latitude, double longitude) {
        return new Coordinates(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;

        Coordinates other = (Coordinates) o;

        return Objects.equals(latitude, other.latitude) &&
                Objects.equals(longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "{\"latitude\": " + latitude + ", \"longitude\": " + longitude + "}";
    }
}
