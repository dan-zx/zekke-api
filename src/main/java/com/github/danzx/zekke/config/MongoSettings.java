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
package com.github.danzx.zekke.config;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * MongoDB configuration settings.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class MongoSettings {

    private final ServerAddress address;
    private final MongoCredential credential;
    private final String database;

    /**
     * Constructor.
     * 
     * @param host MongoDB database host.
     * @param port MongoDB database host port.
     * @param database MongoDB database.
     * @param userName MongoDB database username.
     * @param password MongoDB database user password.
     */
    public MongoSettings(@Value("${mongodb.host}") String host,
                         @Value("${mongodb.port}") Integer port,
                         @Value("${mongodb.db}") @NotBlank String database, 
                         @Value("${mongodb.db.user}") @NotBlank String userName, 
                         @Value("${mongodb.db.password}") @NotBlank String password) {
        address = port == null ? new ServerAddress(host) : new ServerAddress(host, port);
        credential = MongoCredential.createCredential(userName, database, password.toCharArray());
        this.database = database;
    }

    public ServerAddress getAddress() {
        return address;
    }

    public MongoCredential getCredential() {
        return credential;
    }

    public String getDatabase() {
        return database;
    }
}
