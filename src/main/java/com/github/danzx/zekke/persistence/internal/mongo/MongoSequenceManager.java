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
package com.github.danzx.zekke.persistence.internal.mongo;

import static com.github.danzx.zekke.persistence.internal.mongo.MongoSequence.COLLECTION_NAME;
import static com.github.danzx.zekke.persistence.internal.mongo.MongoSequence.Fields.FUNCTION_RESULT;
import static com.github.danzx.zekke.persistence.internal.mongo.MongoSequence.Fields.ID;
import static com.github.danzx.zekke.persistence.internal.mongo.MongoSequence.Fields.SEQ;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.github.danzx.zekke.persistence.internal.Sequence;
import com.github.danzx.zekke.persistence.internal.SequenceManager;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Manages and retrieves sequences values using the sequences collection.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class MongoSequenceManager implements SequenceManager {

    private final MongoDatabase database;
    private final MongoCollection<Document> sequencesCollection;
    
    public @Inject MongoSequenceManager(MongoClient mongoClient, @Value("${mongodb.db}") String databaseName) {
        database = mongoClient.getDatabase(databaseName);
        sequencesCollection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public long getCurrentSequenceValue(Sequence sequence) {
        Document sequenceDocument = sequencesCollection.find(eq(ID, sequence.id())).first();
        Number sequenceValue = sequenceDocument.get(SEQ, Number.class);
        return sequenceValue.longValue();
    }

    @Override
    public long getNextSequenceValue(Sequence sequence) {
        Document returnDocument = database.runCommand(new Document("$eval", sequence.nextValueFunctionCall()));
        return returnDocument.get(FUNCTION_RESULT, Number.class).longValue();
    }

    @Override
    public void setSequenceValue(Sequence sequence, long newValue) {
        sequencesCollection.updateOne(eq(ID, sequence.id()), new Document("$set", new Document(SEQ, newValue)));
    }
}
