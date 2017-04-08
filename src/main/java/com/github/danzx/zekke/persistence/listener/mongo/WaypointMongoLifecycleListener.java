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
package com.github.danzx.zekke.persistence.listener.mongo;

import static com.github.danzx.zekke.util.Collections2.isNullOrEmpty;

import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.PostPersist;
import org.mongodb.morphia.annotations.PreSave;

import com.github.danzx.zekke.domain.Waypoint;

import com.mongodb.DBObject;

/**
 * MongoDB lifecycle listener for the Waypoints collection.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class WaypointMongoLifecycleListener extends MongoLifecycleListener<Waypoint> {

    @Override
    @PostLoad
    protected void postLoad(Waypoint entity, DBObject dbObj) {
        if (!isNullOrEmpty(entity.getPaths())) entity.getPaths().stream().forEach(path -> path.setFromWaypoint(entity.getId()));
    }

    @PreSave
    @Override
    protected  void preSave(Waypoint entity, DBObject dbObj) {
        if(entity.getId() == null) dbObj.put("_id", "getNextSequenceValue('waypoint_id')");
    }

    @Override
    @PostPersist
    protected void postPersist(Waypoint entity, DBObject dbObj) {
        if(entity.getId() == null) entity.setId((Long) dbObj.get("_id"));
    }
}
