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
package com.github.danzx.zekke.mongo.config;

import static com.github.danzx.zekke.util.Strings.isNullOrBlank;
import static com.github.danzx.zekke.util.Strings.requireNonBlank;

import java.util.Optional;

import com.github.danzx.zekke.base.Buildable;
import com.github.danzx.zekke.util.Strings;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Mongo DB database location and settings to access the application database.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class MongoDbSettings {

    private final String database;
    private final ServerAddress address;
    private final MongoCredential credential;

    private MongoDbSettings(Builder builder) {
        this.database = builder.database;
        address = new ServerAddress(builder.host, builder.port);
        if (!isNullOrBlank(builder.user)) {
            credential = builder.password == null ? 
                    MongoCredential.createCredential(builder.user, builder.database, Strings.EMPTY.toCharArray()) :
                    MongoCredential.createCredential(builder.user, builder.database, builder.password.toCharArray());
        } else credential = null;
    }

    public static Builder ofDatabase(String database) {
        return new Builder(database);
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

    public static class Builder implements Buildable<MongoDbSettings> {
        private static final String DEFAULT_HOST = "localhost";
        private static final int DEFAULT_PORT = 27017;

        private final String database;
        private String host = DEFAULT_HOST;
        private int port = DEFAULT_PORT;
        private String user;
        private String password;

        private Builder(String database) {
            this.database = requireNonBlank(database);
        }

        public Builder locatedAt(String host) {
            this.host = requireNonBlank(host);
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public MongoDbSettings build() {
            return new MongoDbSettings(this);
        }
    }
}
