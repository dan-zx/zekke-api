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
package com.github.danzx.zekke.data.filter.waypoint;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint.Type;

/**
 * Filter options for waypoints near a given location.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class LocationWaypointFilterOptions extends BaseWaypointFilterOptions {

    private Builder builder;

    private LocationWaypointFilterOptions(Builder builder) {
        super(builder);
        this.builder = builder;
    }

    public Coordinates getLocation() {
        return builder.location;
    }

    public Optional<Integer> getMaxDistance() {
        return Optional.ofNullable(builder.maxDistance);
    }

    @Override
    public String toString() {
        return  "{ "
                + "limit: " + getLimit() + ", "
                + "waypointType: " + getWaypointType() + ", "
                + "maxDistance: " + getMaxDistance() + ", "
                + "location: " + getLocation()
                + " }";
    }

    public static class Builder extends BaseWaypointFilterOptions.Builder {
        private final Coordinates location;
        private Integer maxDistance;

        public static Builder nearLocation(Coordinates location) {
            return new Builder(location);
        }

        private Builder(Coordinates location) {
            this.location = requireNonNull(location);
        }

        public Builder maximumSearchDistance(Integer maxDistance) {
            this.maxDistance = maxDistance;
            return this;
        }

        @Override
        public Builder byType(Type waypointType) {
            super.byType(waypointType);
            return this;
        }

        @Override
        public Builder limitResulsTo(Integer limit) {
            super.limitResulsTo(limit);
            return this;
        }

        @Override
        public LocationWaypointFilterOptions build() {
            return new LocationWaypointFilterOptions(this);
        }
    }
}
