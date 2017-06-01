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

import javax.validation.constraints.NotNull;

import com.github.danzx.zekke.constraint.FloatRange;
import com.github.danzx.zekke.domain.transformer.mongo.Coordinates2GeoJsonPointTransformer;

import org.mongodb.morphia.geo.Point;

/**
 * Represents a geographic point in the planet.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Coordinates {

    public static final double MAX_LATITUDE = 90;
    public static final double MIN_LATITUDE = -MAX_LATITUDE;
    public static final double MAX_LONGITUDE = 180;
    public static final double MIN_LONGITUDE = -MAX_LONGITUDE;

    private static final Coordinates2GeoJsonPointTransformer TRANSFORMER = new Coordinates2GeoJsonPointTransformer();
    private static final String LAT_LNG_SEPARATOR = ",";

    @NotNull @FloatRange(min = MIN_LATITUDE,  max = MAX_LATITUDE)  private final Double latitude;
    @NotNull @FloatRange(min = MIN_LONGITUDE, max = MAX_LONGITUDE) private final Double longitude;

    /**
     * Only used by third party frameworks.
     * 
     * @deprecated in favor of {@link #ofLatLng(Double, Double)}
     * @param latitude is a decimal number between -90.0 and 90.0.
     * @param longitude is a decimal number between -180.0 and 180.0.
     */
    @Deprecated
    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Factory constructor.
     * 
     * @param latitude is a decimal number between -90.0 and 90.0.
     * @param longitude is a decimal number between -180.0 and 180.0.
     * @return a new Coordinates object.
     */
    public static Coordinates ofLatLng(Double latitude, Double longitude) {
        return new Coordinates(latitude, longitude);
    }

    /**
     * Creates a Coordinates object from the string representation as described in
     * {@link #toString()}.
     * 
     * @param value a string that specifies a Coordinates object.
     * @return a Coordinates object with the specified value.
     * @throws IllegalArgumentException if the value does not conform to the string representation
     *         as described in {@link #toString()}.
     */
    public static Coordinates fromString(String value) {
        if (value == null) return null;
        String[] latlng = value.split(LAT_LNG_SEPARATOR);
        if (latlng.length != 2) throw new IllegalArgumentException("Coordinates bad format");
        try {
            return ofLatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Componets of Coordinates must be doubles");
        }
    }

    /** Converts a GeoJson point to a Coordinates object. */
    static Coordinates valueOf(Point point) {
        return TRANSFORMER.convertBtoA(point);
    }

    /** Converts this object to a GeoJson point. */
    Point toGeoJsonPoint() {
        return TRANSFORMER.convertAtoB(this);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isCoordinatesEqualTo((Coordinates) obj);
    }

    /**
     * Use this method to complete your equals method.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the argument; {@code false} otherwise.
     */
    protected boolean isCoordinatesEqualTo(Coordinates other) {
        return Objects.equals(latitude, other.latitude) && 
               Objects.equals(longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    /**
     * Returns a String object representing this Coordinates object. 
     * 
     * @return {@code <latitude_value>,<longitude_value>}
     */
    @Override
    public String toString() {
        return latitude + LAT_LNG_SEPARATOR + longitude;
    }
}
