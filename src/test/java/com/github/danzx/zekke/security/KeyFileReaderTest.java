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
package com.github.danzx.zekke.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class KeyFileReaderTest {

    @Test
    public void shouldReadTestKey() {
        byte[] key = "Hellol".getBytes();
        assertThat(KeyFileReader.fromClasspath("keys/test1.key").getKey()).isNotNull().isNotEmpty().isEqualTo(key);
    }

    @Test
    @Parameters(method = "filePaths")
    public void shouldThrowIllegalArgumentExceptionWhenFileIsInvalid(String path) {
        assertThatThrownBy(() -> KeyFileReader.fromClasspath(path).getKey()).isInstanceOf(IllegalArgumentException.class);
    }

    protected Object[][] filePaths() {
        return new Object[][] {
            {"unexisting/path/key.file"},
            {"keys/empty.key"},
            {"keys/invalid.key"}
        };
    }
}
