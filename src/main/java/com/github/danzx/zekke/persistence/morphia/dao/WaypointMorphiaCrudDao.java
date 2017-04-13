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
package com.github.danzx.zekke.persistence.morphia.dao;

import static java.util.Objects.requireNonNull;

import java.util.List;

import java.util.Optional;

import javax.inject.Inject;

import org.mongodb.morphia.Datastore;
import org.springframework.stereotype.Repository;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.persistence.internal.mongo.MongoSequence;
import com.github.danzx.zekke.persistence.internal.mongo.MongoSequenceManager;
import com.github.danzx.zekke.persistence.morphia.MorphiaWaypoint;

/**
 * Waypoint Morphia CRUD DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class WaypointMorphiaCrudDao extends BaseMorphiaCrudDao<MorphiaWaypoint, Long> implements WaypointDao<MorphiaWaypoint> {

    private final MongoSequenceManager sequenceManager;

    public @Inject WaypointMorphiaCrudDao(Datastore datastore, MongoSequenceManager sequenceManager) {
        super(datastore, MorphiaWaypoint.class);
        requireNonNull(sequenceManager, "sequenceManager shouldn't be null in order set ids to waypoints");
        this.sequenceManager = sequenceManager;
    }

    @Override
    public MorphiaWaypoint save(MorphiaWaypoint waypoint) {
        requireNonNull(waypoint, "MorphiaWaypoint shouldn't be null in order to be saved");
        if (waypoint.getId() == null) {
            long id = sequenceManager.getNextSequenceValue(MongoSequence.WAYPOINT_ID);
            waypoint.setId(id);
        }
        return super.save(waypoint);
    }

    @Override
    public Optional<MorphiaWaypoint> findNearest(Coordinates location, double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MorphiaWaypoint> findPoisByNameLike(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MorphiaWaypoint> findPoisWithinBox(Coordinates bottomLeftPoint, Coordinates upperRightPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> findNamesWithinBoxLike(String name, Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates) {
        // TODO Auto-generated method stub
        return null;
    }
}
