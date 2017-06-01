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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class) 
public class MongoDbSettingsTest {

    private static final String ANY_STR = "any";
    private static final int ANY_INT = 80;
    @Test
    public void shouldConstructorThrowNullPointExceptionWhenHostIsNull() {
        assertThatThrownBy(() -> new MongoDbSettings(null, ANY_STR, ANY_INT, ANY_STR, ANY_STR)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldConstructorThrowNullPointExceptionWhenDatabaseIsNull() {
        assertThatThrownBy(() -> new MongoDbSettings(ANY_STR, null, ANY_INT, ANY_STR, ANY_STR)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Parameters(source = BlankStringProvider.class) 
    public void shouldConstructorThrowIllegalArgumentExceptionWhenDatabaseIsBlank(String blank) {
        assertThatThrownBy(() -> new MongoDbSettings(ANY_STR, blank, ANY_INT, ANY_STR, ANY_STR)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Parameters(method = "usersAndPasswords")
    public void shouldConstructorObject(String user, String password, boolean isCredentialPresent) {
        MongoDbSettings settings = new MongoDbSettings(ANY_STR, ANY_STR, ANY_INT, user, password);
        assertThat(settings.getDatabase()).isNotNull().isEqualTo(ANY_STR);
        assertThat(settings.getAddress()).isNotNull().extracting(ServerAddress::getHost, ServerAddress::getPort).containsExactly(ANY_STR, ANY_INT);
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
