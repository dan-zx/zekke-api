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

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Waypoint.Type;

/**
 * Filter options for waypoints.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class WaypointFilterOptions extends BaseWaypointFilterOptions {

    private Builder builder;

    private WaypointFilterOptions(Builder builder) {
        super(builder);
        this.builder = builder;
    }

    public Optional<BoundingBox> getBoundingBox() {
        return Optional.ofNullable(builder.boundingBox);
    }

    public Optional<String> getNameQuery() {
        return Optional.ofNullable(builder.nameQuery);
    }

    public boolean onlyIdAndName() {
        return builder.onlyIdAndName;
    }

    @Override
    public String toString() {
        return  "{ "
                + "limit: " + getLimit() + ", "
                + "waypointType: " + getWaypointType() + ", "
                + "onlyIdAndName: " + onlyIdAndName() + ", "
                + "nameQuery: " + getNameQuery() + ", "
                + "boundingBox: " + getBoundingBox()
                + " }";
    }

    public static class Builder extends BaseWaypointFilterOptions.Builder {
        private BoundingBox boundingBox;
        private String nameQuery;
        private boolean onlyIdAndName;

        public Builder withinBoundingBox(BoundingBox bbox) {
            boundingBox = bbox;
            return this;
        }

        public Builder withNameContaining(String name) {
            nameQuery = Optional.ofNullable(name).map(String::trim).orElse(null);
            return this;
        }

        public Builder onlyIdAndName() {
            onlyIdAndName = true;
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
        public WaypointFilterOptions build() {
            return new WaypointFilterOptions(this);
        }
    }
}
