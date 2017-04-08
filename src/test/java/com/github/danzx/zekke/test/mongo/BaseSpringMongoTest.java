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

import javax.inject.Inject;

import org.bson.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;

import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.github.danzx.zekke.config.MongoSettings;
import com.github.danzx.zekke.test.IntegrationTest;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

@IntegrationTest
public class BaseSpringMongoTest {

    @ClassRule public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Inject private MongoClient mongoClient;
    @Inject private MongoSettings mongoDbSettings;

    private MongoDatabase database;

    @Before
    public void before() throws Exception {
        database = mongoClient.getDatabase(mongoDbSettings.getDatabase());
        for (DatabaseFunction function : DatabaseFunction.values()) {
            initFunction(database, function);
        }
        for (DatabaseCollection collection : DatabaseCollection.values()) {
            initCollection(database, collection);
        }
        // This doesn't work on Fongo :(
        //database.runCommand(new Document("$eval", "db.loadServerScripts()"));
    }

    @After
    public void after() throws Exception {
        database.drop();
    }

    private void initFunction(MongoDatabase database, DatabaseFunction function) {
        database.getCollection("system.js").updateOne(
                new Document("_id", function.functionName()),
                new Document("$set", function.document()),
                new UpdateOptions().upsert(true));
    }

    private void initCollection(MongoDatabase database, DatabaseCollection collection) {
        MongoCollection<Document> mongoCollection = database.getCollection(collection.collectionName());
        mongoCollection.insertMany(collection.documents());
    }
}
