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
package com.github.danzx.zekke.test.mongo;

import com.github.danzx.zekke.mongo.config.MongoDbSettings;
import com.github.danzx.zekke.test.spring.ForIntegration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ForIntegration
public class IntegrationMongoDbConfig {

    @Bean
    public MongoDbSettings mongoDbSettings(@Value("${test.mongodb.db}") String database,
                                           @Value("${test.mongodb.host}") String host,
                                           @Value("${test.mongodb.port}") int port) {
        return MongoDbSettings
                .builderFromDatabase(database)
                .locatedAt(host)
                .withPort(port)
                .build();
    }
}
