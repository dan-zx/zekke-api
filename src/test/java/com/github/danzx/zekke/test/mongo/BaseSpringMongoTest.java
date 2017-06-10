/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.danzx.zekke.test.mongo;

import static com.github.danzx.zekke.persistence.internal.mongo.CommonOperators.EVAL;
import static com.github.danzx.zekke.persistence.internal.mongo.CommonOperators.SET;

import javax.inject.Inject;

import com.github.danzx.zekke.mongo.config.MongoDbSettings;
import com.github.danzx.zekke.persistence.internal.mongo.Fields;
import com.github.danzx.zekke.test.spring.BaseSpringIntegrationTest;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;

import org.junit.After;
import org.junit.Before;

import org.mongodb.morphia.Datastore;

public abstract class BaseSpringMongoTest extends BaseSpringIntegrationTest {

    @Inject private MongoClient mongoClient;
    @Inject private Datastore datastore;
    @Inject private MongoDbSettings mongoSettings;

    private MongoDatabase database;

    @Before
    public void before() throws Exception {
        database = mongoClient.getDatabase(mongoSettings.getDatabase());
        for (DatabaseFunction function : DatabaseFunction.values()) {
            initFunction(database, function);
        }
        for (DatabaseCollection collection : DatabaseCollection.values()) {
            initCollection(database, collection);
        }

        database.runCommand(new Document(EVAL, "db.loadServerScripts()"));
        datastore.ensureIndexes();
    }

    @After
    public void after() throws Exception {
        database.drop();
    }

    private void initFunction(MongoDatabase database, DatabaseFunction function) {
        database.getCollection("system.js").updateOne(
                new Document(Fields.Common.ID, function.functionName()),
                new Document(SET, function.document()),
                new UpdateOptions().upsert(true));
    }

    private void initCollection(MongoDatabase database, DatabaseCollection collection) {
        MongoCollection<Document> mongoCollection = database.getCollection(collection.collectionName());
        mongoCollection.insertMany(collection.documents());
    }
}
