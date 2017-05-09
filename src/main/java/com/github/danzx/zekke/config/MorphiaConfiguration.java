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

import com.mongodb.MongoClient;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Morphia configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Configuration
public class MorphiaConfiguration {

    @Bean
    public Datastore datastore(MongoClient mongoClient, MongoDbSettings mongoSettings) {
        return morphia().createDatastore(mongoClient, mongoSettings.getDatabase());
    }

    @Bean
    public Morphia morphia() {
        return new Morphia().mapPackage("com.github.danzx.zekke.domain");
    }
}
