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

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.persistence.dao.WaypointDao;
import com.github.danzx.zekke.persistence.internal.mongo.Fields;
import com.github.danzx.zekke.persistence.internal.mongo.MongoSequence;
import com.github.danzx.zekke.persistence.internal.mongo.MongoSequenceManager;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Shape;

import org.springframework.stereotype.Repository;

/**
 * Waypoint Morphia CRUD DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class WaypointMorphiaCrudDao extends BaseMorphiaCrudDao<Waypoint, Long> implements WaypointDao {

    private static final int NO_LIMIT = 0;

    private final MongoSequenceManager sequenceManager;

    public @Inject WaypointMorphiaCrudDao(Datastore datastore, MongoSequenceManager sequenceManager) {
        super(datastore, Waypoint.class);
        this.sequenceManager = requireNonNull(sequenceManager);
    }

    @Override
    public void saveOrUpdate(Waypoint waypoint) {
        requireNonNull(waypoint);
        if (waypoint.getId() == null) {
            long id = sequenceManager.getNextSequenceValue(MongoSequence.WAYPOINT_ID);
            waypoint.setId(id);
        }
        super.saveOrUpdate(waypoint);
    }

    @Override
    public List<Waypoint> findOptionallyByTypeAndNameQuery(Optional<Type> waypointType, Optional<String> nameQuery) {
        Query<Waypoint> query = createQuery();
        waypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        nameQuery.ifPresent(name -> query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(name));
        return query.asList();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<Waypoint> findNear(Coordinates location, Optional<Integer> maxDistance, Optional<Integer> limit, Optional<Type> waypointType) {
        requireNonNull(location);
        Query<Waypoint> query = createQuery()
                .field(Fields.Waypoint.LOCATION)
                // A NullPointerException is thrown here but it seems to be working anyways WTF?
                .near(location.toGeoJsonPoint(), maxDistance.orElse(DEFAULT_MAX_DISTANCE))
                // Deprecated but I don't now what other options can be used.
                .limit(limit.orElse(NO_LIMIT));
        waypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        return query.asList();
    }

    @Override
    public List<Waypoint> findWithinBox(BoundingBox bbox, Optional<Type> waypointType, Optional<String> nameQuery, boolean onlyIdAndName) {
        requireNonNull(bbox);
        Query<Waypoint> query = createQuery();
        query.criteria(Fields.Waypoint.LOCATION)
            .within(Shape.box(
                    toShapePoint(bbox.getBottomLeftCoordinates()), 
                    toShapePoint(bbox.getUpperRightCoordinates())
                )
        );
        if (nameQuery.isPresent()) {
            query.and(
                query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(nameQuery.get()),
                query.criteria(Fields.Waypoint.TYPE).equal(Type.POI)
            );
        } else waypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        if (onlyIdAndName) query.project(Fields.Waypoint.NAME, true);
        return query.asList();
    }

    private Shape.Point toShapePoint(Coordinates coordinates) {
        return new Shape.Point(coordinates.getLongitude(), coordinates.getLatitude());
    }
}
