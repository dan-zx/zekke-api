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
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.service.WaypointService;

import org.springframework.stereotype.Service;

/**
 * This implementation forwards all method calls to DAOs. 
 * 
 * @author Daniel Pedraza-Arcega
 */
@Service
public class ForwardingWaypointService implements WaypointService {

    private final WaypointDao dao;

    public @Inject ForwardingWaypointService(WaypointDao dao) {
        requireNonNull(dao, "DAO shouldn't be null in use it in the service");
        this.dao = dao;
    }

    @Override
    public void persist(Waypoint waypoint) {
        dao.saveOrUpdate(waypoint);
    }

    @Override
    public void delete(Waypoint waypoint) {
        requireNonNull(waypoint, "waypoint shouldn't be null in order to be deleted");
        dao.deleteById(waypoint.getId());
    }

    @Override
    public List<Waypoint> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Waypoint> findPoiById(long id) {
        return dao.findPoiById(id);
    }

    @Override
    public Optional<Waypoint> findNearest(Coordinates location, int maxDistance) {
        return dao.findNearest(location, maxDistance);
    }

    @Override
    public Optional<String> findNearestPoiName(Coordinates location, int maxDistance) {
        return dao.findNearestPoiName(location, maxDistance);
    }

    @Override
    public List<Waypoint> findPoisByNameLike(String name) {
        return dao.findPoisByNameLike(name);
    }

    @Override
    public List<Waypoint> findPoisWithinBox(Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates) {
        return dao.findPoisWithinBox(bottomLeftCoordinates, upperRightCoordinates);
    }

    @Override
    public List<String> findPoiNamesWithinBoxLike(String name, Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates) {
        return dao.findPoiNamesWithinBoxLike(name, bottomLeftCoordinates, upperRightCoordinates);
    }
}
