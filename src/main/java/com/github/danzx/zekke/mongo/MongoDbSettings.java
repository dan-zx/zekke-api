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
package com.github.danzx.zekke.mongo;

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.util.Strings.requireNonBlank;

import static com.mongodb.MongoCredential.createCredential;

import java.util.Optional;

import com.github.danzx.zekke.util.Strings;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Mongo DB database location and settings to access the application database.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class MongoDbSettings {

    private final String database;
    private final ServerAddress address;
    private final MongoCredential credential;

    public MongoDbSettings(
            @Value("${mongodb.host}") String host, 
            @Value("${mongodb.db}") String database, 
            @Value("${mongodb.port}") int port, 
            @Value("${mongodb.db.user:#{null}}") String databaseUser,
            @Value("${mongodb.db.password:#{null}}") String databasePassword) {
        requireNonNull(host);
        this.database = requireNonBlank(database);
        address = new ServerAddress(host, port);
        credential = databaseUser != null && databasePassword == null ?
                createCredential(databaseUser, database, Strings.EMPTY.toCharArray()) : 
                databaseUser != null && databasePassword != null ?
                createCredential(databaseUser, database, databasePassword.toCharArray()) : 
                null;
    }

    public String getDatabase() {
        return database;
    }

    public ServerAddress getAddress() {
        return address;
    }

    public Optional<MongoCredential> getCredential() {
        return Optional.ofNullable(credential);
    }
}
