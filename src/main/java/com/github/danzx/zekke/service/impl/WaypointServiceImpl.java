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
package com.github.danzx.zekke.service.impl;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.service.ServiceException;
import com.github.danzx.zekke.service.WaypointService;
import org.springframework.stereotype.Service;

/**
 * Default Waypoint service implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Service
public class WaypointServiceImpl implements WaypointService {

    private final WaypointDao dao;

    public @Inject WaypointServiceImpl(WaypointDao dao) {
        requireNonNull(dao, "DAO shouldn't be null in order to access and persist Waypoints");
        this.dao = dao;
    }

    @Override
    public void persist(Waypoint waypoint) {
        requireNonNull(waypoint, "waypoint shouldn't be null in order to be persisted");
        requireNonNull(waypoint.getType(), "Waypoint must have a type");
        requireNonNull(waypoint.getLocation(), "Waypoint must have a location");
        if (waypoint.getType() == Type.POI && !waypoint.getName().isPresent()) {
            throw new ServiceException.Builder()
                .messageKey("poi.name.null.error")
                .build();
        }
        if (waypoint.getType() == Type.WALKWAY && waypoint.getName().isPresent()) {
            throw new ServiceException.Builder()
                .messageKey("walkway.name.not_null.error")
                .build();
        }
        dao.saveOrUpdate(waypoint);
    }

    @Override
    public void delete(Waypoint waypoint) {
        requireNonNull(waypoint, "waypoint shouldn't be null in order to be deleted");
        dao.deleteById(waypoint.getId());
    }

    @Override
    public Optional<Waypoint> findWaypointById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Waypoint> findWaypoints(WaypointsQuery query) {
        requireNonNull(query, "Query shouldn't be null in order to find waypoints");
        return query.getBoundingBox()
            .map(bbox -> dao.findWithinBox(bbox[0], bbox[1], query.getWaypointType(), query.getNameQuery(), false))
            .orElse(dao.findOptionallyByTypeAndNameQuery(query.getWaypointType(), query.getNameQuery()));
    }

    @Override
    public List<Waypoint> findNearWaypoints(NearWaypointsQuery query) {
        requireNonNull(query, "Query shouldn't be null in order to find waypoints");
        return dao.findNear(query.getLocation(), query.getMaxDistance(), query.getLimit(), query.getWaypointType());
    }

    @Override
    public List<Waypoint> findPoisForNameCompletion(Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates, Optional<String> nameQuery) {
        return dao.findWithinBox(bottomLeftCoordinates, upperRightCoordinates, Optional.of(Type.POI), nameQuery, true);
    }
}
