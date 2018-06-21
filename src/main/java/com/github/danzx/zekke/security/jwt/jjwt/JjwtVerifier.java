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

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.BaseJwtVerifier;
import com.github.danzx.zekke.security.jwt.JwtSettings;
import com.github.danzx.zekke.security.jwt.JwtVerificationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * JWT verifier using Java JWT library.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class JjwtVerifier extends BaseJwtVerifier {

    private static final Logger log = LoggerFactory.getLogger(JjwtVerifier.class);

    private final byte[] signingKey;

    public @Inject JjwtVerifier(JwtSettings jwtSettings) {
        super(jwtSettings.getIssuer());
        signingKey = jwtSettings.getSigningKey();
    }

    @Override
    public void verify(String compactJws, User.Role role) throws JwtVerificationException {
        log.debug("JWT: {}, expectedRole: {}", compactJws, role);
        requireNonNull(compactJws);
        requireNonNull(role);
        Jws<Claims> claims;
        try {
            claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(compactJws);
        } catch (ExpiredJwtException ex) {
            throw new JwtVerificationException.Builder()
                .messageKey("jwt.expired")
                .cause(ex)
                .build();
        } catch (SignatureException ex) {
            throw new JwtVerificationException.Builder()
                .messageKey("jwt.invalid.signature")
                .cause(ex)
                .build();
        }
        verifyIssuer(claims.getBody().getIssuer());
        verifyUserPrivileges(role, claims.getBody().getSubject());
    }
}
