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

import static com.github.danzx.zekke.util.Strings.quoted;

import java.util.Objects;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.EntityListeners;

import com.github.danzx.zekke.persistence.listener.mongo.WaypointMongoLifecycleListener;

/**
 * Represents a location in a map.
 *
 * @author Daniel Pedraza-Arcega
 */
@Entity("waypoints")
@EntityListeners(WaypointMongoLifecycleListener.class)
public class Waypoint extends BaseEntity<Long> {

    public enum Type {POI, WALKWAY}
    
    private String name;
    private Type type;
    
    @Embedded
    private Point location;
    
    @Embedded
    private Set<Path> paths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isWaypointEqualTo((Waypoint) obj);
    }

    /**
     * Use this method to complete your equals method.
     * @see {@link #equals(Object)}
     */
    protected boolean isWaypointEqualTo(Waypoint other) {
        return isEntityEqualTo(other) &&
               Objects.equals(name, other.name) && 
               Objects.equals(location, other.location) &&
               Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, location, type);
    }

    @Override
    public String toString() {
        return "{ _id:" + getId() + ", name:" + quoted(name) + ", location:" + location + ", type:"
                + type + ", paths:" + paths + " }";
    }
}
