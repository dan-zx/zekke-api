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

import java.time.Instant;
import java.util.Date;

import javax.inject.Inject;

import com.github.danzx.zekke.security.jwt.BaseJwtFactory;
import com.github.danzx.zekke.security.jwt.JwtSettings;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * JWT factory using Java JWT library.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class JjwtFactory extends BaseJwtFactory {

    private static final Logger log = LoggerFactory.getLogger(JjwtFactory.class);

    private final String issuer;
    private final SignatureAlgorithm signatureAlgorithm;
    private final byte[] signingKey;

    public @Inject JjwtFactory(JwtSettings jwtSettings) {
        super(jwtSettings.getExpiration());
        issuer = jwtSettings.getIssuer();
        signatureAlgorithm = SignatureAlgorithm.forName(jwtSettings.getSignatureAlgorithm());
        signingKey = jwtSettings.getSigningKey();
    }

    @Override
    protected String createToken(Instant issueTime, Instant expirationTime, String subject) {
        log.debug("JWT { issuedAt: {}, expirtation: {}, subject: {}, issuer: {} }", issueTime, expirationTime, subject, issuer);
        return Jwts.builder()
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey)
                .setIssuer(issuer)
                .setIssuedAt(new Date(issueTime.toEpochMilli()))
                .setExpiration(new Date(expirationTime.toEpochMilli()))
                .compact();
    }
}
