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
package com.github.danzx.zekke.security.jwt;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

import com.github.danzx.zekke.security.KeyFileReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT configuration settings.
 *
 * @author Daniel Pedraza-Arcega
 */
@Component
public class JwtSettings {

    private final Duration expiration;
    private final String issuer;
    private final String signatureAlgorithm;
    private final byte[] signingKey;

    public JwtSettings(
            @Value("${jwt.expiration_expression}") String expirationExpression,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.signature_algorithm}") String signatureAlgorithm,
            @Value("${jwt.key_file_path}") String keyFilePath) {
        this.issuer = requireNonNull(issuer);
        this.signatureAlgorithm = requireNonNull(signatureAlgorithm);
        expiration = Duration.parse(expirationExpression);
        signingKey = KeyFileReader.fromClasspath(keyFilePath).getKey();
    }

    public Duration getExpiration() {
        return expiration;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public byte[] getSigningKey() {
        return signingKey;
    }

    @Override
    public String toString() {
        return "{" +
                " expiration=" + expiration +
                ", issuer=" + issuer +
                ", signatureAlgorithm=" + signatureAlgorithm +
                ", signingKey=" + signingKey +
                " }";
    }
}
