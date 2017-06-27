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

import static java.util.Objects.requireNonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

/**
 * Set of SHA-2 functions.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Sha2HashFunction implements HashFunction {

    private final MessageDigest digest;
    private final Base64.Encoder base64encoder = Base64.getEncoder();
    private static final Map<Algorithm, Sha2HashFunction> FUNCTIONS = new EnumMap<>(Algorithm.class);

    /** Available SHA-2 algorithms. */
    public enum Algorithm { 
        SHA_256, SHA_384, SHA_512;

        private final String name;

        Algorithm() {
            name = name().replace('_', '-');
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Factory method.
     * 
     * @param algorithm one of the available algorithms.
     * @return a Sha2HashFunction.
     */
    public static Sha2HashFunction of(Algorithm algorithm) {
        requireNonNull(algorithm);
        return FUNCTIONS.computeIfAbsent(algorithm, Sha2HashFunction::new);
    }

    private Sha2HashFunction(Algorithm algorithm) {
        try {
            digest = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String hash(String input) {
        requireNonNull(input);
        byte[] hashBytes = digest.digest(input.getBytes());
        byte[] encodedHashBytes = base64encoder.encode(hashBytes);
        return new String(encodedHashBytes);
    }
}
