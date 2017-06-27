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

import java.time.Instant;

import com.github.danzx.zekke.domain.User;

/**
 * Base JWT Factory.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class BaseJwtFactory implements JwtFactory {

    private static final long ONE_MINUTE_IN_SECONDS = 60;

    private final long expirationTimeInMinutes;
    private final String issuer;
    private final SigningKeyHolder signingKeyHolder;

    protected BaseJwtFactory(long expirationTimeInMinutes, String issuer, SigningKeyHolder signingKeyHolder) {
        this.expirationTimeInMinutes = expirationTimeInMinutes;
        this.issuer = requireNonNull(issuer);
        this.signingKeyHolder = requireNonNull(signingKeyHolder);
    }

    @Override
    public String newToken(User.Role role) {
        requireNonNull(role);
        Instant issueTime = Instant.now();
        Instant expirationTime = issueTime.plusSeconds(expirationTimeInMinutes * ONE_MINUTE_IN_SECONDS);
        return createToken(issueTime, expirationTime, role.name());
    }

    /**
     * Creates the actual token.
     * 
     * @param issueTime the time to issue the token.
     * @param expirationTime the time when the token expires.
     * @param subject the subject.
     * @return a compacted JWT.
     */
    protected abstract String createToken(Instant issueTime, Instant expirationTime, String subject);

    protected String getIssuer() {
        return issuer;
    }

    protected SigningKeyHolder getSigningKeyHolder() {
        return signingKeyHolder;
    }
}
