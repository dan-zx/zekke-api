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

import static com.github.danzx.zekke.util.Strings.quoted;

import java.util.List;
import java.util.Objects;

/**
 * Generic location in a map.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Waypoint {

    public enum Type {POI, WALKWAY}
    
    private Integer _id;
    private String name;
    private Point location;
    private String type;
    private List<Path> paths;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type == null ? null : type.name();
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Waypoint)) return false;
        Waypoint other = (Waypoint) obj;
        return Objects.equals(_id, other._id) &&
               Objects.equals(name, other.name) && 
               Objects.equals(location, other.location) &&
               Objects.equals(type, other.type) &&
               Objects.equals(paths, other.paths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, name, location, paths, type);
    }

    @Override
    public String toString() {
        return "{ _id:" + _id + ", name:" + quoted(name) + ", location:" + location + ", type:"
                + type + ", paths:" + paths + " }";
    }
}
