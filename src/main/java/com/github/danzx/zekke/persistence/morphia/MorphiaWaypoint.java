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

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Point;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Path;
import com.github.danzx.zekke.domain.Poi;
import com.github.danzx.zekke.domain.Walkway;
import com.github.danzx.zekke.util.Strings;

/**
 * Morphia Walkway and POI implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Entity(value = "waypoints", noClassnameStored = true)
public class MorphiaWaypoint extends MorphiaEntity<Long> implements Walkway, Poi {

    public enum Type {POI, WALKWAY}

    private Optional<String> name = Optional.empty();
    private Type type;

    @Embedded
    private Point location;

    @Embedded
    private Set<MorphiaPath> paths = new HashSet<>();

    public static MorphiaWaypoint newPoi() {
        MorphiaWaypoint poi = new MorphiaWaypoint();
        poi.setType(Type.POI);
        return poi;
    }

    public static MorphiaWaypoint newWalkway() {
        MorphiaWaypoint poi = new MorphiaWaypoint();
        poi.setType(Type.WALKWAY);
        return poi;
    }

    @Override
    public String getName() {
        if (type == Type.POI) return name.orElseThrow(() -> new IllegalStateException("POIs should have a name"));
        return null;
    }

    @Override
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Objects.requireNonNull(type, "type cannot be null");
        this.type = type;
    }

    @Override
    public Coordinates getLocation() {
        return Coordinates.ofLatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void setLocation(Coordinates location) {
        Objects.requireNonNull(location, "location cannot be null");
        this.location = GeoJson.point(location.getLatitude(), location.getLongitude());
    }

    @Override
    public Set<MorphiaPath> getPaths() {
        return paths;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setPaths(Set<? extends Path> paths) {
        this.paths = paths == null ? new HashSet<>() : (Set<MorphiaPath>) paths;
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
        return isMorphiaWaypointEqualTo((MorphiaWaypoint) obj);
    }

    /**
     * Use this method to complete your equals method.
     * @see {@link #equals(Object)}
     */
    protected boolean isMorphiaWaypointEqualTo(MorphiaWaypoint other) {
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
        return "{ _id:" + getId() + ", name:" + name.map(Strings::quoted).orElse(null) + 
                ", location:" + location + ", type:" + type + ", paths:" + paths + " }";
    }
}
