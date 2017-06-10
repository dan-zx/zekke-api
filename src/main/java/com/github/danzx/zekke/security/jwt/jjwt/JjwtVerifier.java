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

import com.github.danzx.zekke.security.UserRole;
import com.github.danzx.zekke.security.jwt.BaseJwtVerifier;
import com.github.danzx.zekke.security.jwt.JwtVerificationException;
import com.github.danzx.zekke.security.jwt.SigningKeyHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT verifier using Java JWT library.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class JjwtVerifier extends BaseJwtVerifier {

    public @Inject JjwtVerifier(@Value("${jwt.issuer}") String issuer, SigningKeyHolder signingKeyHolder) {
        super(issuer, signingKeyHolder);
    }

    @Override
    public void verify(String compactJws, UserRole role) throws JwtVerificationException {
        requireNonNull(compactJws);
        requireNonNull(role);
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(getSigningKeyHolder().getKey()).parseClaimsJws(compactJws);
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
