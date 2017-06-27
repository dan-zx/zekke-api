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
package com.github.danzx.zekke.ws.rest.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.danzx.zekke.util.Strings;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class BasicAuthorizationHeaderExtractorTest {

    private static final BasicAuthorizationHeaderExtractor EXTRACTOR = new BasicAuthorizationHeaderExtractor();

    @Test
    public void shouldExtractCredentials() {
        String headerInfo = "Basic YWRtaW46cm9vdDEyMzQ=";

        assertThat(EXTRACTOR.getCredentials(headerInfo)).isNotNull().extracting(Credentials::getUserId, Credentials::getPassword).containsOnly("admin", "root1234");
    }

    @Test
    @Parameters(method = "headerInfos")
    public void shouldGetCredentialsThrowIllegalArgumentException(String headerInfo) {
        assertThatThrownBy(() -> EXTRACTOR.getCredentials(headerInfo)).isInstanceOf(IllegalArgumentException.class);
    }

    protected Object[][] headerInfos() {
        return new Object[][] {
            {"YWRtaW46cm9vdDEyMzQ="},
            {"BasicYWRtaW46cm9vdDEyMzQ="},
            {"NotBasic YWRtaW46cm9vdDEyMzQ="},
            {" Basic YWRtaW46cm9vdDEyMzQ="},
            {"BasicNot YWRtaW46cm9vdDEyMzQ="},
            {"Basic dXNlcjpwYXNzd29yZDpvdGhlcg=="}, //user:password:other
            {"Basic dXNlcjo="}, //user:
            {"Basic Og=="}, //:
            {"a random string"},
            {null},
            {Strings.EMPTY},
            {Strings.BLANK_SPACE},
            {Strings.NEW_LINE},
            {Strings.TAB}
        };
    }
}
