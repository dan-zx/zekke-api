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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.time.Instant;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.JwtSettings;
import com.github.danzx.zekke.security.jwt.JwtVerificationException;
import com.github.danzx.zekke.test.spring.BaseSpringTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = JwtSettings.class)
@TestPropertySource(locations = "classpath:test.properties")
public class JjwtVerifierTest extends BaseSpringTest {
    
    
    private static final class SigningKeyPaths {
        private static final String KEY_1 = "keys/test1.key";
        private static final String KEY_2 = "keys/test2.key";

        private SigningKeyPaths() {
            throw new AssertionError();
        }
    }

    private static final class Issuers {
        private static final String TEST = "testIssuer";
        private static final String OTHER = "anotherIssuer";
        
        private Issuers() { 
            throw new AssertionError();
        }
    }

    private @Inject JwtSettings baseJwtSettings;

    @Before
    public void setUp() {
        assertThat(baseJwtSettings).isNotNull();
    }

    @Test
    public void shouldVerifyToken() {
        JjwtFactory jwtFactory = new JjwtFactory(baseJwtSettings);
        User.Role role = User.Role.ADMIN;
        String token = jwtFactory.newToken(role);
        JjwtVerifier jwtVerifier = new JjwtVerifier(baseJwtSettings);
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
                                                               String factoryKeyPath,
                                                               String verifierKeyPath,
                                                               Instant expirationTime,
                                                               String issuerSubject,
                                                               User.Role verificationSubject) {
        JwtSettings factorySettings = JwtSettingsBuilder.fromOther(baseJwtSettings)
                .setIssuer(factoryIssuer)
                .setKeyFilePath(factoryKeyPath)
                .build();
        JwtSettings verifierSettings = JwtSettingsBuilder.fromOther(baseJwtSettings)
                .setIssuer(verifierIssuer)
                .setKeyFilePath(verifierKeyPath)
                .build();
        JjwtFactory jwtFactory = new JjwtFactory(factorySettings);
        JjwtVerifier jwtVerifier = new JjwtVerifier(verifierSettings);
        String token = jwtFactory.createToken(Instant.now(), expirationTime, issuerSubject);
        assertThatThrownBy(() -> jwtVerifier.verify(token, verificationSubject)).isInstanceOf(JwtVerificationException.class);
    }

    protected Object[][] verificationParams() {
        return new Object[][] {
            {Issuers.TEST, Issuers.OTHER, SigningKeyPaths.KEY_1, SigningKeyPaths.KEY_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {Issuers.TEST, Issuers.TEST, SigningKeyPaths.KEY_1, SigningKeyPaths.KEY_2, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {Issuers.TEST, Issuers.TEST, SigningKeyPaths.KEY_1, SigningKeyPaths.KEY_1, Instant.now().plusSeconds(-60L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {Issuers.OTHER, Issuers.TEST, SigningKeyPaths.KEY_1, SigningKeyPaths.KEY_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {Issuers.TEST, Issuers.TEST, SigningKeyPaths.KEY_2, SigningKeyPaths.KEY_1, Instant.now().plusSeconds(30L), User.Role.ADMIN.name(), User.Role.ADMIN},
            {Issuers.TEST, Issuers.TEST, SigningKeyPaths.KEY_1, SigningKeyPaths.KEY_1, Instant.now().plusSeconds(30L), "notARoleSubject", User.Role.ADMIN},
            {Issuers.TEST, Issuers.TEST, SigningKeyPaths.KEY_1, SigningKeyPaths.KEY_1, Instant.now().plusSeconds(30L), User.Role.ANONYMOUS.name(), User.Role.ADMIN}
        };
    }

    private static class JwtSettingsBuilder {
        private String expirationExpression;
        private String issuer;
        private String signatureAlgorithm;
        private String keyFilePath;

        private static JwtSettingsBuilder fromOther(JwtSettings jwtSettings) {
            JwtSettingsBuilder builder = new JwtSettingsBuilder();
            builder.expirationExpression = jwtSettings.getExpiration().toString();
            builder.signatureAlgorithm = jwtSettings.getSignatureAlgorithm();
            return builder;
        }

        public JwtSettingsBuilder setIssuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public JwtSettingsBuilder setKeyFilePath(String keyFilePath) {
            this.keyFilePath = keyFilePath;
            return this;
        }

        public JwtSettings build() {
            return new JwtSettings(expirationExpression, issuer, signatureAlgorithm, keyFilePath);
        }
    }
}
