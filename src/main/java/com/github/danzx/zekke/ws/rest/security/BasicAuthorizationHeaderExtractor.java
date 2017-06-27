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

import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Extracts Basic Authorization HTTP header information.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class BasicAuthorizationHeaderExtractor extends AuthorizationHeaderExtractor {

    public BasicAuthorizationHeaderExtractor() {
        super(Mechanism.BASIC);
    }

    /**
     * Parses the Basic header information. The format should be
     * "Basic {@literal <base64 user:password>}"
     * 
     * @param headerInfo the header information
     * @return an array of strings with the first index is the user and the second is the
     *         password.
     * @throw IllegalArgumentException when the information cannot be extracted.
     */
    public Credentials getCredentials(String headerInfo) {
        return Optional.ofNullable(headerInfo)
            .filter(header -> header.startsWith(getPrefix()))
            .map(header -> header.substring(getPrefix().length()).trim())
            .map(encodedUserIdAndPassword -> Base64.getDecoder().decode(encodedUserIdAndPassword))
            .map(String::new)
            .map(userIdAndPassword -> {
                String[] userIdAndPasswordArray = userIdAndPassword.split(":");
                if (userIdAndPasswordArray.length != 2) throw new IllegalArgumentException("Cannot extract user and password. Header bad format");
                Credentials credentials = new Credentials();
                credentials.setUserId(userIdAndPasswordArray[0]);
                credentials.setPassword(userIdAndPasswordArray[1]);
                return credentials;
            })
            .orElseThrow(() -> new IllegalArgumentException("Cannot extract user and password. Header bad format"));
    }
}
