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

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.security.jwt.JwtFactory;
import com.github.danzx.zekke.service.UserService;
import com.github.danzx.zekke.ws.rest.model.AccessTokenHolder;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage.Type;
import com.github.danzx.zekke.ws.rest.security.BasicAuthorizationHeaderExtractor;
import com.github.danzx.zekke.ws.rest.security.Credentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * JWT Authentication endpoint. 
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@Path(V_1 + "/authentication/jwt/")
public class JwtAuthenticationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEndpoint.class);

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();
    private final JwtFactory jwtFactory;
    private final UserService userService;
    private final BasicAuthorizationHeaderExtractor authorizationHeaderExtractor;

    public @Inject JwtAuthenticationEndpoint(JwtFactory jwtFactory, UserService userService, BasicAuthorizationHeaderExtractor authorizationHeaderExtractor) {
        this.jwtFactory = requireNonNull(jwtFactory);
        this.userService = requireNonNull(userService);
        this.authorizationHeaderExtractor = requireNonNull(authorizationHeaderExtractor);
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
        log.info("GET /authentication/jwt/anonymous");
        String jwt = jwtFactory.newToken(User.Role.ANONYMOUS);
        return AccessTokenHolder.of(jwt);
    }

    /**
     * Authenticates the admin user.
     * 
     * @param authorizationHeader "Basic Authentication" header. 
     * @param clientLocales "Accept-Language" header.
     * @return a 200 OK with a JWT or 401 Unauthorized.
     */
    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateAdmin(@NotNull @HeaderParam("Authorization") String authorizationHeader,
                                      @HeaderParam("Accept-Language") List<Locale> clientLocales) {
        log.info("GET /authentication/jwt/admin -- Authorization={}, Accept-Language={}", authorizationHeader, clientLocales);
        Locale clientLocale = clientLocales.stream().findFirst().orElse(Locale.ROOT);
        Credentials credentials;
        try {
            credentials = authorizationHeaderExtractor.getCredentials(authorizationHeader);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid authorization header", ex);
            Response.Status status = Response.Status.BAD_REQUEST;
            ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(Type.PARAM_VALIDATION)
                .detailMessage(messageSource.getMessage("basic.auth.header.format.error", clientLocale))
                .build();
            return Response
                .status(status)
                .entity(errorMessage)
                .build();
        }
        User.Role userRole;
        try {
            userRole = User.Role.fromUsername(credentials.getUserId());
        } catch (NullPointerException | IllegalArgumentException ex) {
            return loginErrorResponse(clientLocale);
        }
        User user = new User();
        user.setRole(userRole);
        user.setPassword(credentials.getPassword());
        if (!userService.isAdminRegistered(user)) return loginErrorResponse(clientLocale);
        String jwt = jwtFactory.newToken(userRole);
        return Response
                .ok(AccessTokenHolder.of(jwt))
                .build();
    }

    private Response loginErrorResponse(Locale clientLocale) {
        return Response
            .status(Response.Status.UNAUTHORIZED)
            .entity(loginErrorMessage(clientLocale))
            .build();
    }

    private ErrorMessage loginErrorMessage(Locale clientLocale) {
        return new ErrorMessage.Builder()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
                .type(Type.AUTHORIZATION)
                .detailMessage(messageSource.getMessage("authorization.login.error", clientLocale))
                .build();
    }
}
