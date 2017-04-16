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

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;

/**
 * Waypoint business logic service.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface WaypointService extends PersistentService<Waypoint> {

    /**
     * Finds the nearest Waypoint to given location.
     * 
     * @param location a location.
     * @param maxDistance limits the results to those Waypoints that are at most the specified
     *        distance from the location (meters).
     * @return an optional Waypoint.
     */
    Optional<Waypoint> findNearest(Coordinates location, int maxDistance);

    /**
     * Finds the POIs by a name similar to given.
     * 
     * @param name a partial name.
     * @return a list of POIs or an empty list.
     */
    List<Waypoint> findPoisByNameLike(String name);

    /**
     * Finds the POIs that are within the bounds of a rectangle, you must specify the bottom left
     * and top right corners of the rectangle.
     * 
     * @param bottomLeftCoordinates the bottom left coordinates.
     * @param upperRightCoordinates the upper right coordinates.
     * @return a list of POIs or an empty list.
     */
    List<Waypoint> findPoisWithinBox(Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates);

    /**
     * Finds the POI names that are within the bounds of a rectangle, you must specify the bottom
     * left and top right corners of the rectangle.
     * 
     * @param name a partial name.
     * @param bottomLeftPoint the bottom left coordinates.
     * @param upperRightPoint the upper right coordinates.
     * @return a list of POI names or an empty list.
     */
    List<String> findNamesWithinBoxLike(String name, Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates);
}
