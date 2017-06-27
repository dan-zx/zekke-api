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
package com.github.danzx.zekke.security.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.danzx.zekke.security.crypto.Sha2HashFunction.Algorithm;
import org.junit.Test;

public class Sha2HashFunctionTest {

    @Test
    public void shouldHashString() {
        assertThat(Sha2HashFunction.of(Algorithm.SHA_256).hash("lol")).isNotBlank().isEqualTo("BxI+H0gjVsQV9oRAejuHI+ELLLvAuPzWKCxJ03ycGrw=");
    }
}
