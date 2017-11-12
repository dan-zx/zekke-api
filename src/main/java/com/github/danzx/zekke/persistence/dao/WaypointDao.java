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
package com.github.danzx.zekke.persistence.dao;

import java.util.List;

import com.github.danzx.zekke.data.filter.waypoint.LocationWaypointFilterOptions;
import com.github.danzx.zekke.data.filter.waypoint.WaypointFilterOptions;
import com.github.danzx.zekke.domain.Waypoint;

/**
 * Waypoint CRUD DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface WaypointDao extends CrudDao<Waypoint, Long> {

    /** 500 meters. */
    int DEFAULT_MAX_DISTANCE = 500;

    /**
     * Filters waypoints with several options.
     * 
     * @param filterOptions the filter options.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findFiltered(WaypointFilterOptions filterOptions);

    /**
     * Finds waypoints near a location and filters them with several options.
     * 
     * @param filterOptions the filter options.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findNearALocationFiltered(LocationWaypointFilterOptions filterOptions);
}
