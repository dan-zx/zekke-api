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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Contains a bottom left coordinates and upper right coordinates of a rectangle.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class BoundingBox {

    private static final String COORDINATES_SEPARATOR = ";";

    @NotNull @Valid private final Coordinates bottomCoordinates;
    @NotNull @Valid private final Coordinates topCoordinates;

    /**
     * Only used by third party frameworks.
     * 
     * @deprecated in favor of {@link #ofBottomTop(Coordinates, Coordinates)}
     * @param bottomCoordinates the bottom coordinates.
     * @param topCoordinates the top coordinates.
     */
    @Deprecated
    public BoundingBox(Coordinates bottomCoordinates, Coordinates topCoordinates) {
        this.bottomCoordinates = bottomCoordinates;
        this.topCoordinates = topCoordinates;
    }

    /**
     * Factory constructor.
     * 
     * @param bottomCoordinates the bottom coordinates.
     * @param topCoordinates the top coordinates.
     * @return a new BoundingBox.
     */
    public static BoundingBox ofBottomTop(Coordinates bottomCoordinates, Coordinates topCoordinates) {
        return new BoundingBox(bottomCoordinates, topCoordinates);
    }

    /**
     * Creates a BoundingBox from the string representation as described in {@link #toString()}.
     * 
     * @param value a string that specifies a BoundingBox.
     * @return a BoundingBox with the specified value.
     * @throws IllegalArgumentException if the value does not conform to the string representation
     *         as described in {@link #toString()}.
     */
    public static BoundingBox fromString(String value) {
        if (value == null) return null;
        String[] bottomAndUpper = value.split(COORDINATES_SEPARATOR);
        if (bottomAndUpper.length != 2) throw new IllegalArgumentException("BoundingBox bad format");
        Coordinates bottomCoordinates = Coordinates.fromString(bottomAndUpper[0]);
        Coordinates topCoordinates = Coordinates.fromString(bottomAndUpper[1]);
        return ofBottomTop(bottomCoordinates, topCoordinates);
    }

    public Coordinates getBottomCoordinates() {
        return bottomCoordinates;
    }

    public Coordinates getTopCoordinates() {
        return topCoordinates;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bottomCoordinates, topCoordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isBoundingBoxEqualTo((BoundingBox)obj);
    }

    /**
     * Use this method to complete your equals method.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the argument; {@code false} otherwise.
     */
    protected boolean isBoundingBoxEqualTo(BoundingBox other) {
        return Objects.equals(bottomCoordinates, other.bottomCoordinates) &&
               Objects.equals(topCoordinates, other.topCoordinates);
    }

    /**
     * Returns a String object representing this BoundingBox. 
     * 
     * @return {@code <bottom_latitude>,<left_longitude>;<top_latitude>,<right_longitude>}
     */
    @Override
    public String toString() {
        return bottomCoordinates + COORDINATES_SEPARATOR + topCoordinates;
    }
}
