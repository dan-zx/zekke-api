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

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.service.WaypointService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Default Waypoint service implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Validated @Service
public class WaypointServiceImpl implements WaypointService {

    private static final Logger log = LoggerFactory.getLogger(WaypointServiceImpl.class);

    private final WaypointDao dao;

    public @Inject WaypointServiceImpl(WaypointDao dao) {
        this.dao = requireNonNull(dao);
    }

    @Override
    public void persist(Waypoint waypoint) {
        log.debug("persistWaypoint: {}", waypoint);
        dao.saveOrUpdate(waypoint);
    }

    @Override
    public Optional<Waypoint> findWaypointById(long id) {
        log.debug("findWaypointById: {}", id);
        return dao.findById(id);
    }

    @Override
    public List<Waypoint> findWaypoints(WaypointsQuery query) {
        log.debug("findWaypoints: {}", query);
        return Optional.ofNullable(query.getBoundingBox())
            .map(bbox -> dao.findWithinBox(bbox, query.getWaypointType(), query.getNameQuery(), false))
            .orElse(dao.findOptionallyByTypeAndNameQuery(query.getWaypointType(), query.getNameQuery()));
    }

    @Override
    public List<Waypoint> findNearWaypoints(NearWaypointsQuery query) {
        log.debug("findNearWaypoints: {}", query);
        return dao.findNear(query.getLocation(), query.getMaxDistance(), query.getLimit(), query.getWaypointType());
    }

    @Override
    public List<Waypoint> findPoisForNameCompletion(BoundingBox bbox, String nameQuery) {
        log.debug("findPoisForNameCompletion: { bbox: {}, nameQuery: {} }", bbox, nameQuery);
        return dao.findWithinBox(bbox, Type.POI, nameQuery, true);
    }

    @Override
    public boolean delete(Waypoint waypoint) {
        log.debug("deleteWaypoint: {}", waypoint);
        return dao.deleteById(waypoint.getId());
    }
}
