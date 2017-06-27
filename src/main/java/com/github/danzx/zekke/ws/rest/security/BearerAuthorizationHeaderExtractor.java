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

import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Extracts Bearer Authorization HTTP header information.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class BearerAuthorizationHeaderExtractor extends AuthorizationHeaderExtractor {

    public BearerAuthorizationHeaderExtractor() {
        super(Mechanism.BEARER);
    }

    /**
     * Parses the header information. The format should be "Bearer {@literal <token>}"
     * 
     * @param headerInfo the header information
     * @return the token.
     * @throw IllegalArgumentException when the token cannot be extracted.
     */
    public String getToken(String headerInfo) {
        return Optional.ofNullable(headerInfo)
            .filter(header -> header.startsWith(getPrefix()))
            .map(header -> header.substring(getPrefix().length()))
            .orElseThrow(() -> new IllegalArgumentException("Cannot extract token. Header bad format"));
    }
}
