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
package com.github.danzx.zekke.ws.rest.patch.jsonpatch;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.danzx.zekke.ws.rest.MediaTypes;
import com.github.danzx.zekke.ws.rest.patch.ObjectPatch;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class JsonPatchReaderTest {

    private static final JsonPatchReader READER;

    static {
        READER = new JsonPatchReader();
        READER.setObjectMapper(new ObjectMapper());
    }

    @Test
    public void shouldCreateObjectPatchInstance() throws Exception {
        InputStream in = new ByteArrayInputStream("[]".getBytes());
        assertThat(READER.readFrom(null, null, null, null, null, in)).isNotNull();
    }

    @Test
    @Parameters(method = "isReadableTestParams")
    public void isReadableShouldReturnExpectedWithArguments(Class<?> type, MediaType mediaType, boolean expected) {
        assertThat(READER.isReadable(type, null, null, mediaType)).isEqualTo(expected);
    }

    protected Object[][] isReadableTestParams() {
        return new Object[][] {
            {ObjectPatch.class, MediaTypes.APPLICATION_JSON_PATCH_TYPE, true},
            {ObjectPatch.class, MediaType.APPLICATION_JSON_TYPE, false},
            {Object.class, MediaTypes.APPLICATION_JSON_PATCH_TYPE, false},
            {ObjectPatch.class, MediaType.TEXT_XML_TYPE, false},
            {Object.class, MediaType.TEXT_XML_TYPE, false}
        };
    }
}
