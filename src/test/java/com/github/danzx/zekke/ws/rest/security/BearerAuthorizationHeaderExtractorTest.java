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
public class BearerAuthorizationHeaderExtractorTest {

    private static final BearerAuthorizationHeaderExtractor EXTRACTOR = new BearerAuthorizationHeaderExtractor();

    @Test
    public void shouldExtractToken() {
        String expectedToken = "aaaa.bbbb.cccc";
        String headerInfo = "Bearer " + expectedToken;

        assertThat(EXTRACTOR.getToken(headerInfo)).isNotBlank().isEqualTo(expectedToken);
    }

    @Test
    @Parameters(method = "headerInfos")
    public void shouldGetTokenThrowIllegalArgumentException(String headerInfo) {
        assertThatThrownBy(() -> EXTRACTOR.getToken(headerInfo)).isInstanceOf(IllegalArgumentException.class);
    }

    protected Object[][] headerInfos() {
        return new Object[][] {
            {"aaaa.bbbb.cccc"},
            {"Beareraaaa.bbbb.cccc"},
            {"NotBearer aaaa.bbbb.cccc"},
            {" Bearer aaaa.bbbb.cccc"},
            {"BearerNot aaaa.bbbb.cccc"},
            {"a random string"},
            {null},
            {Strings.EMPTY},
            {Strings.BLANK_SPACE},
            {Strings.NEW_LINE},
            {Strings.TAB}
        };
    }
}
