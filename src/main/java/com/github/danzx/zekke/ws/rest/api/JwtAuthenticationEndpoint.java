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
package com.github.danzx.zekke.ws.rest.api;

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.ws.rest.ApiVersions.V_1;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.JwtFactory;
import com.github.danzx.zekke.ws.rest.model.AccessTokenHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * JWT Authentication endpoint. 
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@Path(V_1 + "/authentications/jwt/")
public class JwtAuthenticationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEndpoint.class);

    private final JwtFactory jwtFactory;

    public @Inject JwtAuthenticationEndpoint(JwtFactory jwtFactory) {
        this.jwtFactory = requireNonNull(jwtFactory);
    }

    /**
     * Authenticates any user anonymously.
     * 
     * @return a JWT.
     */
    @GET
    @Path("/anonymous")
    @Produces(MediaType.APPLICATION_JSON)
    public AccessTokenHolder authenticateAnonymously() {
        log.info("GET /authentications/jwt/anonymous");
        String jwt = jwtFactory.newToken(User.Role.ANONYMOUS);
        return AccessTokenHolder.of(jwt);
    }
}
