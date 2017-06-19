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

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class) 
public class MongoDbSettingsTest {

    private static final String ANY_STR = "any";

    @Test
    @Parameters(method = "usersAndPasswords")
    public void shouldConstructorObject(String user, String password, boolean isCredentialPresent) {
        MongoDbSettings settings = MongoDbSettings.ofDatabase(ANY_STR)
            .withUser(user)
            .withPassword(password)
            .build();
        assertThat(settings.getDatabase()).isNotNull().isEqualTo(ANY_STR);
        assertThat(settings.getAddress()).isNotNull().extracting(ServerAddress::getHost, ServerAddress::getPort).containsExactly("localhost", 27017);
        assertThat(settings.getCredential().isPresent()).isEqualTo(isCredentialPresent);
        char[] passwordArray = password == null ? new char[0] : password.toCharArray();
        settings.getCredential().ifPresent(credential -> assertThat(credential).extracting(MongoCredential::getUserName, MongoCredential::getPassword).containsExactly(user, passwordArray));
    }

    protected Object[] usersAndPasswords() {
        return new Object[][] {
            {null, null, false},
            {null, ANY_STR, false},
            {ANY_STR, ANY_STR, true},
            {ANY_STR, null, true},
        };
    }
}
