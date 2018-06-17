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
package com.github.danzx.zekke.ws.rest.security.jwt.filter;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.security.jwt.JwtVerificationException;
import com.github.danzx.zekke.security.jwt.JwtVerifier;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage.Type;
import com.github.danzx.zekke.ws.rest.security.BearerAuthorizationHeaderExtractor;
import com.github.danzx.zekke.ws.rest.security.RequireRoleAccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authenticate user using JWT protocol.
 * 
 * @author Daniel Pedraza-Arcega
 */
@RequireRoleAccess
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private @Context ResourceInfo resourceInfo;
    private @Context JwtVerifier jwtVerifier;
    private @Context BearerAuthorizationHeaderExtractor authorizationHeaderExtractor;

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        log.debug("Accept-Languages={}", requestContext.getAcceptableLanguages());
        Locale clientLocale = requestContext.getAcceptableLanguages().stream().findFirst().orElse(Locale.ROOT);
        String headerInfo = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        log.debug("{}={}", HttpHeaders.AUTHORIZATION, headerInfo);
        String token;
        try {
            token = authorizationHeaderExtractor.getToken(headerInfo);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid authorization header", ex);
            Response.Status status = Response.Status.BAD_REQUEST;
            ErrorMessage errorMessage = new ErrorMessage.Builder()
                    .statusCode(status.getStatusCode())
                    .type(Type.PARAM_VALIDATION)
                    .detailMessage(messageSource.getMessage("bearer.auth.header.format.error", clientLocale))
                    .build();
            requestContext.abortWith(Response
                        .status(status)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(errorMessage)
                        .build());
            return;
        }

        User.Role requiredRole = getRequiredRoleToAccessResource();
        try {
            jwtVerifier.verify(token, requiredRole);
        } catch (JwtVerificationException ex) {
            log.error("JWT is invalid", ex);
            Response.Status status = Response.Status.UNAUTHORIZED;
            ErrorMessage errorMessage = new ErrorMessage.Builder()
                    .statusCode(status.getStatusCode())
                    .type(Type.AUTHORIZATION)
                    .detailMessage(ex.getMessage(clientLocale))
                    .build();
            requestContext.abortWith(Response
                        .status(status)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(errorMessage)
                        .build());
        }
    }

    private User.Role getRequiredRoleToAccessResource() {
        Optional<User.Role> roleInClassAnnotation = Optional.of(resourceInfo.getResourceClass())
                .flatMap(resourceClass -> Optional.ofNullable(resourceClass.getAnnotation(RequireRoleAccess.class)))
                .map(RequireRoleAccess::roleRequired);

        Optional<User.Role> roleInMethodAnnotation = Optional.of(resourceInfo.getResourceMethod())
                .flatMap(resourceMethod -> Optional.ofNullable(resourceMethod.getAnnotation(RequireRoleAccess.class)))
                .map(RequireRoleAccess::roleRequired);

        return roleInMethodAnnotation
                .orElseGet(() -> roleInClassAnnotation
                        .orElseThrow(() -> {
                            String location = resourceInfo.getResourceClass().getName() + "." + resourceInfo.getResourceMethod().getName() + "()";
                            log.error("@RequireRoleAccess annotation missing. Filter cannot work here: {}", location);
                            return new UnsupportedOperationException("@RequireRoleAccess annotation missing. Filter cannot work here: " + location);
                        }));
    }

    public void setResourceInfo(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public void setJwtVerifier(JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    public void setAuthorizationHeaderExtractor(BearerAuthorizationHeaderExtractor authorizationHeaderExtractor) {
        this.authorizationHeaderExtractor = authorizationHeaderExtractor;
    }
}
