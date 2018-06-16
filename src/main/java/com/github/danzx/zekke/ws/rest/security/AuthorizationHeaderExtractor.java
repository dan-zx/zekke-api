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

import static java.util.Objects.requireNonNull;

import com.github.danzx.zekke.util.Strings;

/**
 * Base Authorization Header extractor class.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class AuthorizationHeaderExtractor {

    protected enum Mechanism { BASIC, BEARER }
    
    private final String prefix;

    protected AuthorizationHeaderExtractor(Mechanism mechanism) {
        requireNonNull(mechanism);
        char firstChar = mechanism.name().charAt(0);
        prefix = firstChar + mechanism.name().toLowerCase().substring(1, mechanism.name().length()) + Strings.BLANK_SPACE;
    }

    protected String getPrefix() {
        return prefix;
    }
}
