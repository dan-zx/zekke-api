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
package com.github.danzx.zekke.persistence.morphia;

import java.util.Objects;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import com.github.danzx.zekke.domain.Path;

/**
 * Morphia Path implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Embedded
public class MorphiaPath implements Path {

    private Long fromWaypoint;
    
    @Property("to_waypoint")
    private Long toWaypoint;

    private Double distance;

    public long getFromWaypoint() {
        return fromWaypoint;
    }

    public void setFromWaypoint(long fromWaypoint) {
        this.fromWaypoint = fromWaypoint;
    }

    public long getToWaypoint() {
        return toWaypoint;
    }

    public void setToWaypoint(long toWaypoint) {
        this.toWaypoint = toWaypoint;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isPathEqualTo((MorphiaPath) obj);
    }

    /**
     * Use this method to complete your equals method.
     * @see {@link #equals(Object)}
     */
    protected boolean isPathEqualTo(MorphiaPath other) {
        return Objects.equals(fromWaypoint, other.fromWaypoint) &&
               Objects.equals(toWaypoint, other.toWaypoint) &&
               Objects.equals(distance, other.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromWaypoint, toWaypoint, distance);
    }

    @Override
    public String toString() {
        return "{ from_waypoint: " + fromWaypoint + ", to_waypoint:" + toWaypoint + ", distance:" + distance + " }";
    }
}