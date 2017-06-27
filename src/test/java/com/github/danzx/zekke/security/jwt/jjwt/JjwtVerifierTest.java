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
package com.github.danzx.zekke.security.jwt.jjwt;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.time.Instant;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.JwtVerificationException;
import com.github.danzx.zekke.security.jwt.SigningKeyHolder;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class JjwtVerifierTest {
    
    private static final SigningKeyHolder KEY_HOLDER_1 = new SigningKeyHolder("keys/test1.key");
    private static final SigningKeyHolder KEY_HOLDER_2 = new SigningKeyHolder("keys/test2.key");

    @Test
    public void shouldVerifyToken() {
        String issuer = "testIssuer";
        long expirationTimeInMinutes = 1L;
        JjwtFactory jwtFactory = new JjwtFactory(expirationTimeInMinutes, issuer, KEY_HOLDER_1);
        User.Role role = User.Role.ADMIN;
        String token = jwtFactory.newToken(role);
        JjwtVerifier jwtVerifier = new JjwtVerifier(issuer, KEY_HOLDER_1);
        try {
            jwtVerifier.verify(token, role);
        } catch (JwtVerificationException ex) {
            fail("Shouldn't have failed", ex);
        }
    }

    @Test
    @Parameters(method = "verificationParams")
    public void shouldVerifyTokenThrowJwtVerificationException(String factoryIssuer,
                                                               String verifierIssuer,
                                                               SigningKeyHolder factoryKeyHolder,
                                                               SigningKeyHolder verifierKeyHolder,
                                                               Instant expirationTime,
                                                               String issuerSubject,
                                                               User.Role verificationSubject) {
        JjwtFactory jwtFactory = new JjwtFactory(1L, factoryIssuer, factoryKeyHolder);
        String token = jwtFactory.createToken(Instant.now(), expirationTime, issuerSubject);
        JjwtVerifier jwtVerifier = new JjwtVerifier(verifierIssuer, verifierKeyHolder);
        assertThatThrownBy(() -> jwtVerifier.verify(token, verificationSubject)).isInstanceOf(JwtVerificationException.class);
    }

    protected Object[][] verificationParams() {
        return new Object[][] {
            {"testIssuer", "anotherIssuer", KEY_HOLDER_1, KEY_HOLDER_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {"testIssuer", "testIssuer", KEY_HOLDER_1, KEY_HOLDER_2, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {"testIssuer", "testIssuer", KEY_HOLDER_1, KEY_HOLDER_1, Instant.now().plusSeconds(-60L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {"testIssuer", "testIssuer", KEY_HOLDER_1, KEY_HOLDER_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ANONYMOUS},
            {"anotherIssuer", "testIssuer", KEY_HOLDER_1, KEY_HOLDER_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {"testIssuer", "testIssuer", KEY_HOLDER_2, KEY_HOLDER_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {"testIssuer", "testIssuer", KEY_HOLDER_1, KEY_HOLDER_1, Instant.now().plusSeconds(30L), "notARoleSubject", User.Role.ADMIN},
            {"testIssuer", "testIssuer", KEY_HOLDER_1, KEY_HOLDER_1, Instant.now().plusSeconds(30L), User.Role.ANONYMOUS.name(), User.Role.ADMIN}
        };
    }
}
