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

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.persistence.internal.mongo.CommonOperators.EVAL;
import static com.github.danzx.zekke.persistence.internal.mongo.CommonOperators.SET;
import static com.github.danzx.zekke.persistence.internal.mongo.MongoSequence.COLLECTION_NAME;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import com.github.danzx.zekke.mongo.config.MongoDbSettings;
import com.github.danzx.zekke.persistence.internal.Sequence;
import com.github.danzx.zekke.persistence.internal.SequenceManager;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

/**
 * Manages and retrieves sequences values using the sequences collection.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class MongoSequenceManager implements SequenceManager {

    private static final Logger log = LoggerFactory.getLogger(MongoSequenceManager.class);

    private final MongoDatabase database;
    private final MongoCollection<Document> sequencesCollection;
    
    public @Inject MongoSequenceManager(MongoClient mongoClient, MongoDbSettings mongoSettings) {
        requireNonNull(mongoClient);
        requireNonNull(mongoSettings);
        database =  mongoClient.getDatabase(mongoSettings.getDatabase());
        sequencesCollection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public long getCurrentSequenceValue(Sequence sequence) {
        log.debug("currval: {}", sequence);
        requireNonNull(sequence);
        Document sequenceDocument = sequencesCollection.find(eq(Fields.Sequence.ID, sequence.id())).first();
        Number sequenceValue = sequenceDocument.get(Fields.Sequence.SEQ, Number.class);
        return sequenceValue.longValue();
    }

    @Override
    public long getNextSequenceValue(Sequence sequence) {
        log.debug("nextval: {}", sequence);
        requireNonNull(sequence);
        Document returnDocument = database.runCommand(new Document(EVAL, sequence.nextValueFunctionCall()));
        return returnDocument.get(Fields.Function.RESULT, Number.class).longValue();
    }

    @Override
    public void setSequenceValue(Sequence sequence, long newValue) {
        log.debug("setval: {}->{}", sequence, newValue);
        requireNonNull(sequence);
        sequencesCollection.updateOne(eq(Fields.Sequence.ID, sequence.id()), new Document(SET, new Document(Fields.Sequence.SEQ, newValue)));
    }
}
