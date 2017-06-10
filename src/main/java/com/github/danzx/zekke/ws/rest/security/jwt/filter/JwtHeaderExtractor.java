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

import java.util.Optional;

/**
 * JWT header extractor utility.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class JwtHeaderExtractor {

    private static final String BEARER_TAG = "Bearer ";

    private JwtHeaderExtractor() {
        throw new AssertionError();
    }

    /**
     * Parses the JWT header information. The format should be "Bearer {@literal <token>}"
     * 
     * @param headerInfo the header information
     * @return the JWT.
     * @throw IllegalArgumentException when the JWT cannot be extracted.
     */
    static String getToken(String headerInfo) {
        return Optional.ofNullable(headerInfo)
                .filter(header -> header.startsWith(BEARER_TAG))
                .map(header -> header.substring(BEARER_TAG.length()).trim())
                .orElseThrow(() -> new IllegalArgumentException("Cannot extract token from header"));
    }
}
