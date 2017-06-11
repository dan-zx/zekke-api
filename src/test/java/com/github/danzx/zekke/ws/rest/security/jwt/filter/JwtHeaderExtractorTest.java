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
package com.github.danzx.zekke.ws.rest.security.jwt.filter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import com.github.danzx.zekke.util.Strings;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class JwtHeaderExtractorTest {

    @Test
    public void shouldExtractToken() {
        String expectedToken = "aaaa.bbbb.cccc";
        String headerInfo = "Bearer " + expectedToken;
        
        try {
            JwtHeaderExtractor.getToken(headerInfo);
        } catch (IllegalArgumentException ex) {
            fail("Shouldn't have failed", ex);
        }
    }

    @Test
    @Parameters(method = "headerInfos")
    public void shouldExtractTokenThrowIllegalArgumentException(String headerInfo) {
        assertThatThrownBy(() -> JwtHeaderExtractor.getToken(headerInfo)).isInstanceOf(IllegalArgumentException.class);
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
