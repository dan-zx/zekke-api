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

import java.util.Objects;

/**
 * A connection between two waypoints.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Path {

    private Integer to_waypoint;
    private Double distance;

    public Integer getToWaypoint() {
        return to_waypoint;
    }

    public void setToWaypoint(Integer to_waypoint) {
        this.to_waypoint = to_waypoint;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Path)) return false;
        Path other = (Path) obj;
        return Objects.equals(to_waypoint, other.to_waypoint) && 
               Objects.equals(distance, other.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to_waypoint, distance);
    }

    @Override
    public String toString() {
        return "{ to_waypoint:" + to_waypoint + ", distance:" + distance + " }";
    }
}
