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

import java.util.Optional;

import com.github.danzx.zekke.data.filter.FilterOptions;
import com.github.danzx.zekke.domain.Waypoint;

/**
 * Base filter options for waypoints.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class BaseWaypointFilterOptions extends FilterOptions {

    private Builder builder;

    BaseWaypointFilterOptions(Builder builder) {
        super(builder);
        this.builder = builder;
    }

    public Optional<Waypoint.Type> getWaypointType() {
        return Optional.ofNullable(builder.waypointType);
    }

    static abstract class Builder extends FilterOptions.Builder {
        private Waypoint.Type waypointType;

        public Builder byType(Waypoint.Type waypointType) {
            this.waypointType = waypointType;
            return this;
        }

        @Override
        public Builder limitResulsTo(Integer limit) {
            super.limitResulsTo(limit);
            return this;
        }
    }
}
