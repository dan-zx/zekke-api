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

import static com.github.danzx.zekke.config.Profiles.DEVELOPMENT;

import static com.mongodb.MongoCredential.createCredential;

import static java.util.Collections.singletonList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * MongoDB configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Configuration
@Profile(DEVELOPMENT)
public class DevelopmentMongoDbConfig {

    @Bean
    public MongoClient mongoClient(@Value("${mongodb.host}") String host,
                                   @Value("${mongodb.port}") Integer port,
                                   @Value("${mongodb.db}") String database, 
                                   @Value("${mongodb.db.user}") String userName, 
                                   @Value("${mongodb.db.password}") String password) {
        ServerAddress address = new ServerAddress(host, port);
        MongoCredential credential = createCredential(userName, database, password.toCharArray());
        return new MongoClient(address, singletonList(credential));
    }
}
