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
import com.github.danzx.zekke.transformer.Transformer;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.geo.Point;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Shape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

/**
 * Waypoint Morphia CRUD DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class WaypointMorphiaCrudDao extends BaseMorphiaCrudDao<Waypoint, Long> implements WaypointDao {

    private static final Logger log = LoggerFactory.getLogger(WaypointMorphiaCrudDao.class);

    private final MongoSequenceManager sequenceManager;
    private final Transformer<Coordinates, Point> coordinatesTransformer;
    private final Transformer<BoundingBox, Shape> boundingBoxTransformer;

    public @Inject WaypointMorphiaCrudDao(Datastore datastore, 
                                          MongoSequenceManager sequenceManager, 
                                          Transformer<Coordinates, Point> coordinatesTransformer,
                                          Transformer<BoundingBox, Shape> boundingBoxTransformer) {
        super(datastore, Waypoint.class);
        this.sequenceManager = requireNonNull(sequenceManager);
        this.coordinatesTransformer = requireNonNull(coordinatesTransformer);
        this.boundingBoxTransformer = requireNonNull(boundingBoxTransformer);
    }

    @Override
    public void saveOrUpdate(Waypoint waypoint) {
        requireNonNull(waypoint);
        if (waypoint.getId() == null) {
            long id = sequenceManager.getNextSequenceValue(MongoSequence.WAYPOINT_ID);
            log.debug("New id: {}", id);
            waypoint.setId(id);
        }
        super.saveOrUpdate(waypoint);
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<Waypoint> findOptionallyByTypeAndNameQuery(Type waypointType, String nameQuery, Integer limit) {
        log.debug("type: {}, name: {}, limit: {}", waypointType, nameQuery, limit);
        Optional<Type> optionalWaypointType = Optional.ofNullable(waypointType);
        Optional<String> optionalNameQuery = Optional.ofNullable(nameQuery);
        Optional<Integer> optionalLimit = Optional.ofNullable(limit);
        Query<Waypoint> query = createQuery();
        optionalWaypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        optionalNameQuery.ifPresent(name -> query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(name));
        optionalLimit.ifPresent(query::limit);
        return query.asList();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<Waypoint> findNear(Coordinates location, Integer maxDistance, Integer limit, Type waypointType) {
        log.debug("location: {}, maxDistance: {}, limit: {}, type: {}", location, maxDistance, limit, waypointType);
        requireNonNull(location);
        Optional<Integer> optionalMaxDistance = Optional.ofNullable(maxDistance);
        Optional<Integer> optionalLimit = Optional.ofNullable(limit);
        Optional<Type> optionalWaypointType = Optional.ofNullable(waypointType);
        Query<Waypoint> query = createQuery()
                .field(Fields.Waypoint.LOCATION)
                // A NullPointerException is thrown here but it seems to be working anyways WTF?
                .near(coordinatesTransformer.convertAtoB(location), optionalMaxDistance.orElse(DEFAULT_MAX_DISTANCE));
        // Deprecated but I don't now what other options can be used.
        optionalLimit.ifPresent(query::limit);
        optionalWaypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        return query.asList();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<Waypoint> findWithinBox(BoundingBox bbox, Type waypointType, String nameQuery, boolean onlyIdAndName, Integer limit) {
        log.debug("bbox: {}, type: {}, nameQuery: {}, onlyIdAndName: {}, limit: {}", bbox, waypointType, nameQuery, onlyIdAndName, limit);
        requireNonNull(bbox);
        Optional<Type> optionalWaypointType = Optional.ofNullable(waypointType);
        Optional<String> optionalNameQuery = Optional.ofNullable(nameQuery);
        Optional<Integer> optionalLimit = Optional.ofNullable(limit);
        Query<Waypoint> query = createQuery();
        query.criteria(Fields.Waypoint.LOCATION).within(boundingBoxTransformer.convertAtoB(bbox));
        if (optionalNameQuery.isPresent()) {
            query.and(
                query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(optionalNameQuery.get()),
                query.criteria(Fields.Waypoint.TYPE).equal(Type.POI)
            );
        } else optionalWaypointType.ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        if (onlyIdAndName) query.project(Fields.Waypoint.NAME, true);
        optionalLimit.ifPresent(query::limit);
        return query.asList();
    }
}
