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

import javax.inject.Inject;

import com.github.danzx.zekke.data.filter.waypoint.LocationWaypointFilterOptions;
import com.github.danzx.zekke.data.filter.waypoint.WaypointFilterOptions;
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
import org.mongodb.morphia.query.FindOptions;
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
    public List<Waypoint> findFiltered(WaypointFilterOptions filterOptions) {
        log.debug("Filter: {}", filterOptions);
        requireNonNull(filterOptions);
        Query<Waypoint> query = createQuery();
        filterOptions.getBoundingBox().ifPresent(bbox -> query.and(query.criteria(Fields.Waypoint.LOCATION).within(boundingBoxTransformer.convertAtoB(bbox))));
        if (filterOptions.getNameQuery().isPresent()) {
            String nameQuery = filterOptions.getNameQuery().get();
            query.and(
                query.criteria(Fields.Waypoint.NAME).containsIgnoreCase(nameQuery),
                query.criteria(Fields.Waypoint.TYPE).equal(Type.POI)
            );
        } else filterOptions.getWaypointType().ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        if (filterOptions.onlyIdAndName()) query.project(Fields.Waypoint.NAME, true);
        return filterOptions.getLimit()
                .map(limit -> new FindOptions().limit(limit))
                .map(query::asList)
                .orElseGet(query::asList);
    }

    @Override
    public List<Waypoint> findNearALocationFiltered(LocationWaypointFilterOptions filterOptions) {
        log.debug("Filter: {}", filterOptions);
        requireNonNull(filterOptions);
        Query<Waypoint> query = createQuery()
                .field(Fields.Waypoint.LOCATION)
                // A NullPointerException is thrown here but it seems to be working anyways WTF?
                .near(coordinatesTransformer.convertAtoB(filterOptions.getLocation()), filterOptions.getMaxDistance().orElse(DEFAULT_MAX_DISTANCE));
        filterOptions.getWaypointType().ifPresent(type -> query.and(query.criteria(Fields.Waypoint.TYPE).equal(type)));
        return filterOptions.getLimit()
                .map(limit -> new FindOptions().limit(limit))
                .map(query::asList)
                .orElseGet(query::asList);
    }
}
