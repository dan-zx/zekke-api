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
package com.github.danzx.zekke.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.github.danzx.zekke.constraint.NotNullId;
import com.github.danzx.zekke.data.filter.waypoint.LocationWaypointFilterOptions;
import com.github.danzx.zekke.data.filter.waypoint.WaypointFilterOptions;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;

/**
 * Waypoint business logic service.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface WaypointService {

    /**
     * Persists the given waypoint into the underlying datastore.
     * 
     * @param waypoint an element to persist.
     * @throws ServiceException if the given waypoint is of type {@link Type#WALKWAY} and a name is
     *         present.
     * @throws ServiceException if the given waypoint is of type {@link Type#POI} and a name is not
     *         present.
     */
    void persist(@NotNull @Valid Waypoint waypoint);

    /**
     * Finds a waypoint by its id.
     * 
     * @param id an id.
     * @return the optional waypoint.
     */
    Optional<Waypoint> findWaypointById(long id);

    /**
     * Filters waypoints with several options.
     * 
     * @param filterOptions the filter options.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findWaypoints(@NotNull WaypointFilterOptions filterOptions);

    /**
     * Finds waypoints near a location and filters them with several options.
     * 
     * @param filterOptions the filter options.
     * @return a list of waypoints or an empty list.
     */
    List<Waypoint> findWaypointsNearALocation(@NotNull LocationWaypointFilterOptions filterOptions);

    /**
     * Deletes the given waypoint from the underlying datastore.
     *  
     * @param waypoint the waypoint to delete.
     * @return true if the object with given was deleted; otherwise false.
     */
    boolean delete(@NotNull @NotNullId Waypoint waypoint);
}
