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
import com.github.danzx.zekke.security.jwt.SigningKeyHolder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT factory using Java JWT library.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class JjwtFactory extends BaseJwtFactory {

    public @Inject JjwtFactory(@Value("${jwt.expiration}") long expirationTimeInMinutes,
                               @Value("${jwt.issuer}") String issuer,
                               SigningKeyHolder signingKeyHolder) {
        super(expirationTimeInMinutes, issuer, signingKeyHolder);
    }

    @Override
    protected String createToken(Instant issueTime, Instant expirationTime, String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, getSigningKeyHolder().getKey())
                .setIssuer(getIssuer())
                .setIssuedAt(new Date(issueTime.toEpochMilli()))
                .setExpiration(new Date(expirationTime.toEpochMilli()))
                .compact();
    }
}
