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
package com.github.danzx.zekke.persistence.dao.morphia;

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
import org.mongodb.morphia.geo.GeoJson;
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
    public List<Waypoint> findOptionallyByTypeAndNameQuery(Type waypointType, String nameQuery) {
        Optional<Type> optionalWaypointType = Optional.ofNullable(waypointType);
        Optional<String> optionalNameQuery = Optional.ofNullable(nameQuery);
        Query<Waypoint> query = createQuery();
        optionalWaypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        optionalNameQuery.ifPresent(name -> query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(name));
        return query.asList();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<Waypoint> findNear(Coordinates location, Integer maxDistance, Integer limit, Type waypointType) {
        requireNonNull(location);
        Optional<Integer> optionalMaxDistance = Optional.ofNullable(maxDistance);
        Optional<Integer> optionalLimit = Optional.ofNullable(limit);
        Optional<Type> optionalWaypointType = Optional.ofNullable(waypointType);
        Query<Waypoint> query = createQuery()
                .field(Fields.Waypoint.LOCATION)
                // A NullPointerException is thrown here but it seems to be working anyways WTF?
                .near(GeoJson.point(location.getLatitude(), location.getLongitude()), optionalMaxDistance.orElse(DEFAULT_MAX_DISTANCE))
                // Deprecated but I don't now what other options can be used.
                .limit(optionalLimit.orElse(NO_LIMIT));
        optionalWaypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        return query.asList();
    }

    @Override
    public List<Waypoint> findWithinBox(BoundingBox bbox, Type waypointType, String nameQuery, boolean onlyIdAndName) {
        requireNonNull(bbox);
        Optional<Type> optionalWaypointType = Optional.ofNullable(waypointType);
        Optional<String> optionalNameQuery = Optional.ofNullable(nameQuery);
        Query<Waypoint> query = createQuery();
        query.criteria(Fields.Waypoint.LOCATION)
            .within(Shape.box(
                    toShapePoint(bbox.getBottomCoordinates()), 
                    toShapePoint(bbox.getTopCoordinates())
                )
        );
        if (optionalNameQuery.isPresent()) {
            query.and(
                query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(optionalNameQuery.get()),
                query.criteria(Fields.Waypoint.TYPE).equal(Type.POI)
            );
        } else optionalWaypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        if (onlyIdAndName) query.project(Fields.Waypoint.NAME, true);
        return query.asList();
    }

    private Shape.Point toShapePoint(Coordinates coordinates) {
        return new Shape.Point(coordinates.getLongitude(), coordinates.getLatitude());
    }
}
