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

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmbeddedMongo {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedMongo.class);
    private static final MongodStarter MONGO_STARTER = MongodStarter.getInstance(mongodConfig());

    private final String bindIp;
    private final int port;

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongo;

    public EmbeddedMongo(@Value("${mongodb.host}") String bindIp,
                         @Value("${mongodb.port}") int port) {
        this.bindIp = bindIp;
        this.port = port;
    }

    @PostConstruct
    public void start() throws UnknownHostException, IOException {
        mongodExe = MONGO_STARTER.prepare(
                new MongodConfigBuilder()
                    .version(Version.Main.V3_4)
                    .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                    .build());
        mongod = mongodExe.start();
        mongo = new MongoClient(bindIp, port);
    }

    @PreDestroy
    public void stop() {
        mongod.stop();
        mongodExe.stop();
    }

    public MongoClient getMongo() {
        return mongo;
    }

    private static IRuntimeConfig mongodConfig() {
        return new RuntimeConfigBuilder()
            .defaultsWithLogger(Command.MongoD, LOGGER)
            .build();
    }
}
