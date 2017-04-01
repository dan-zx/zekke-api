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

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.mongodb.morphia.Datastore;

import org.springframework.stereotype.Repository;

import com.github.danzx.zekke.domain.Point;
import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.persistence.dao.WaypointDao;

/**
 * Waypoint Morphia CRUD DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class WaypointMorphiaCrudDao extends BaseMorphiaCrudDao<Waypoint, Long> implements WaypointDao {

    public @Inject WaypointMorphiaCrudDao(Datastore datastore) {
        super(datastore, Waypoint.class);
    }

    @Override
    public Optional<Waypoint> findNearest(Point location, double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Waypoint> findPoisByNameLike(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Waypoint> findPoisWithinBox(Point bottomLeftPoint, Point upperRightPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> findNamesWithinBoxLike(String name, Point bottomLeftCoordinates, Point upperRightCoordinates) {
        // TODO Auto-generated method stub
        return null;
    }
}
