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

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import com.github.danzx.zekke.util.Strings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class) 
public class MongoDbSettingsTest {

    private static class DefaultValues {
        private static final String HOST     = "localhost";
        private static final int    PORT     = 27017;
        private static final char[] PASSWORD = new char[0];
    }

    @Test
    @Parameters(method = "usersAndPasswords")
    public void shouldConstructorObjectFromBuilder(String database, String host, Integer port, String user, String password) {
        MongoDbSettings settings = MongoDbSettings.builderFromDatabase(database)
                .locatedAt(host)
                .withPort(port)
                .withUser(user)
                .withPassword(password)
                .build();
        assertMongoDbSettings(settings, database, host, port, user, password);
    }

    @Test
    @Parameters(method = "validUris")
    public void shouldConstructorObjectFromUri(String uri, String database, String host, int port, String user, String password) {
        MongoDbSettings settings = MongoDbSettings.fromUri(uri);
        assertMongoDbSettings(settings, database, host, port, user, password);
    }

    @Test
    @Parameters(method = "malformedUris")
    public void shouldFromUriThrowExceptionWhenUriIsInvalid(String uri, Class<? extends Exception> throwableClass) {
        assertThatThrownBy(() -> MongoDbSettings.fromUri(uri)).isInstanceOf(throwableClass);
    }

    private void assertMongoDbSettings(MongoDbSettings settings, String database, String host, Integer port, String user, String password) {
        assertThat(settings).isNotNull();
        assertThat(settings.getDatabase()).isNotNull().isEqualTo(database);
        String expectedHost = Optional.ofNullable(host).orElse(DefaultValues.HOST);
        int expectedPort = Optional.ofNullable(port).orElse(DefaultValues.PORT);
        assertThat(settings.getAddress()).isNotNull().extracting(ServerAddress::getHost, ServerAddress::getPort).containsExactly(expectedHost, expectedPort);
        if (user != null) {
            char[] expectedPassword = Optional.ofNullable(password).map(String::toCharArray).orElse(DefaultValues.PASSWORD);
            assertThat(settings.getCredential().isPresent()).isTrue();
            assertThat(settings.getCredential().get()).extracting(MongoCredential::getUserName, MongoCredential::getPassword).containsExactly(user, expectedPassword);
        } else assertThat(settings.getCredential().isPresent()).isFalse();
    }

    protected Object[] usersAndPasswords() {
        return new Object[][] {
            {"database", "host", 27132, "user", "password"},
            {"database", "host", 27132, "user", null},
            {"database", "host", 27132, null, "password"},
            {"database", "host", 27132, null, null},
            {"database", "host", null, "user", "password"},
            {"database", "host", null, "user", "password"},
            {"database", null, 27132, "user", "password"},
            {"database", null, 27132, "user", null},
            {"database", null, null, null, "password"},
            {"database", null, null, null, null},
        };
    }

    protected Object[] validUris() {
        return new Object[][] {
            {"mongodb://user:password@host:27132/database", "database", "host", 27132, "user", "password"},
            {"mongodb://user:@host:27132/database", "database", "host", 27132, null, null},
            {"mongodb://@host:27132/database", "database", "host", 27132, null, null},
            {"mongodb:///database", "database", "localhost", 27017, null, null}
        };
    }

    protected Object[] malformedUris() {
        return new Object[][] {
            {null, NullPointerException.class},
            {Strings.EMPTY, IllegalArgumentException.class},
            {Strings.BLANK_SPACE, IllegalArgumentException.class},
            {Strings.NEW_LINE, IllegalArgumentException.class},
            {Strings.TAB, IllegalArgumentException.class},
            {"://", IllegalArgumentException.class}
        };
    }
}