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

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.github.danzx.zekke.util.Strings;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.geo.Point;
import org.mongodb.morphia.utils.IndexType;

/** 
 * Represents a location in a map. 
 * 
 * @author Daniel Pedraza-Arcega 
 */
@Entity(value = "waypoints", noClassnameStored = true)
@Indexes(
        @Index(fields = @Field(value = "location", type = IndexType.GEO2DSPHERE))
)
public class Waypoint extends BaseEntity<Long> {

    public enum Type {
        POI, WALKWAY;

        private final String pluralName;

        Type() {
            pluralName = name() + "S";
        }

        public String pluralName() {
            return pluralName;
        }

        public static Type fromPluralName(String value, boolean ignoreCase) {
            requireNonNull(value);
            return Arrays.stream(Type.values())
                    .filter(type -> ignoreCase ? type.pluralName.equalsIgnoreCase(value) : type.pluralName.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No WaypointType was found with the value " + value));
        }
    }

    private String name;
    private Type type;

    @Embedded private Point location;
    @Embedded private Set<Path> paths = new HashSet<>();

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = requireNonNull(type);
    }

    public Coordinates getLocation() {
        return Coordinates.valueOf(location);
    }

    public void setLocation(Coordinates location) {
        requireNonNull(location);
        this.location = location.toGeoJsonPoint();
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths == null ? new HashSet<>() : paths;
    }

    @PostLoad
    protected void postLoad() {
        paths.stream().forEach(path -> path.setFromWaypoint(getId()));
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
        return "{ _id:" + getId() + ", name:" + getName().map(Strings::quoted).orElse(null) + 
                ", location:" + location + ", type:" + type + ", paths:" + paths + " }";
    }
}
