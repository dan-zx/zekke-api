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
package com.github.danzx.zekke.test.config;

import org.springframework.boot.test.context.TestConfiguration;

import com.github.danzx.zekke.config.DataConfig;
import com.github.danzx.zekke.config.MongoSettings;

import com.github.fakemongo.Fongo;

import com.mongodb.MongoClient;

@TestConfiguration
public class TestDataConfig extends DataConfig {

    @Override
    public MongoClient mongoClient(MongoSettings mongoDbSettings) {
        return new Fongo(mongoDbSettings.getDatabase()).getMongo();
    }
}
