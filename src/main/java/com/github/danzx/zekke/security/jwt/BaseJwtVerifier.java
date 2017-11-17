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

import com.github.danzx.zekke.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base JWT verifier.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class BaseJwtVerifier implements JwtVerifier {

    private static final Logger log = LoggerFactory.getLogger(BaseJwtVerifier.class);

    private final String issuer;
    private final SigningKeyHolder signingKeyHolder;

    public BaseJwtVerifier(String issuer, SigningKeyHolder signingKeyHolder) {
        this.issuer = requireNonNull(issuer);
        this.signingKeyHolder = requireNonNull(signingKeyHolder);
    }

    protected void verifyIssuer(String issuer) throws JwtVerificationException {
        log.debug("{} == {}", this.issuer, issuer);
        if (!this.issuer.equals(issuer)) {
            throw new JwtVerificationException.Builder()
                .messageKey("jwt.invalid.issuer")
                .build();
        }
    }

    protected void verifyUserPrivileges(User.Role expectedRole, String subject) throws JwtVerificationException {
        User.Role subjectRole;
        try {
            subjectRole = User.Role.valueOf(subject);
        } catch (IllegalArgumentException ex) {
            throw new JwtVerificationException.Builder()
                .messageKey("unknown.role.error")
                .cause(ex)
                .build();
        }
        log.debug("{} >= {}", subjectRole, expectedRole);
        if (subjectRole.hasLessAuthorityThan(expectedRole)) {
            throw new JwtVerificationException.Builder()
                .messageKey("authorization.error")
                .build();
        }
    }

    protected SigningKeyHolder getSigningKeyHolder() {
        return signingKeyHolder;
    }
}
