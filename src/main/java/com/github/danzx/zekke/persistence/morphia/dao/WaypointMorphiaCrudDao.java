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

import static com.github.danzx.zekke.persistence.morphia.dao.WaypointMorphiaCrudDao.Fields.LOCATION;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.persistence.internal.mongo.CommonFields;
import com.github.danzx.zekke.persistence.internal.mongo.MongoSequence;
import com.github.danzx.zekke.persistence.internal.mongo.MongoSequenceManager;

import org.mongodb.morphia.Datastore;

import org.springframework.stereotype.Repository;

/**
 * Waypoint Morphia CRUD DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class WaypointMorphiaCrudDao extends BaseMorphiaCrudDao<Waypoint, Long> implements WaypointDao {

    protected static class Fields extends CommonFields {
        protected static final String NAME = "name";
        protected static final String TYPE = "type";
        protected static final String LOCATION = "location";
        protected static final String PATHS = "paths";
        protected Fields() {}
    }

    private final MongoSequenceManager sequenceManager;

    public @Inject WaypointMorphiaCrudDao(Datastore datastore, MongoSequenceManager sequenceManager) {
        super(datastore, Waypoint.class);
        requireNonNull(sequenceManager, "sequenceManager shouldn't be null in order set ids to waypoints");
        this.sequenceManager = sequenceManager;
    }

    @Override
    public Waypoint save(Waypoint waypoint) {
        requireNonNull(waypoint, "MorphiaWaypoint shouldn't be null in order to be saved");
        if (waypoint.getId() == null) {
            long id = sequenceManager.getNextSequenceValue(MongoSequence.WAYPOINT_ID);
            waypoint.setId(id);
        }
        return super.save(waypoint);
    }

    @Override
    public Optional<Waypoint> findNearest(Coordinates location, int maxDistance) {
        requireNonNull(location, "location shouldn't be null in find a waypoint");
        return Optional.ofNullable(
                getDatastore().createQuery(getCollectionClass())
                    .field(LOCATION)
                    .near(location.toGeoJsonPoint(), maxDistance)
                    .get());
    }

    @Override
    public List<Waypoint> findPoisByNameLike(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Waypoint> findPoisWithinBox(Coordinates bottomLeftPoint, Coordinates upperRightPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> findNamesWithinBoxLike(String name, Coordinates bottomLeftCoordinates, Coordinates upperRightCoordinates) {
        // TODO Auto-generated method stub
        return null;
    }
}
