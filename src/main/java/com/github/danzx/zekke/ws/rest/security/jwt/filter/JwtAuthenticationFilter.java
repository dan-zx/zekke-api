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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;

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

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.security.UserRole;
import com.github.danzx.zekke.security.jwt.JwtVerificationException;
import com.github.danzx.zekke.security.jwt.JwtVerifier;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage.Type;
import com.github.danzx.zekke.ws.rest.security.RequireRoleAccess;

/**
 * Authenticate user using JWT protocol.
 * 
 * @author Daniel Pedraza-Arcega
 */
@RequireRoleAccess
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private @Context ResourceInfo resourceInfo;
    private @Context JwtVerifier jwtVerifier;

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Locale clientLocale = requestContext.getAcceptableLanguages().stream().findFirst().orElse(Locale.ROOT);
        String token;
        try {
            token = JwtHeaderExtractor.getToken(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));
        } catch (IllegalArgumentException ex) {
            Response.Status status = Response.Status.UNAUTHORIZED;
            ErrorMessage errorMessage = new ErrorMessage.Builder()
                    .statusCode(status.getStatusCode())
                    .type(Type.AUTHORIZATION)
                    .detailMessage(messageSource.getMessage("authorization.invalid.header", clientLocale))
                    .build();
            requestContext.abortWith(Response
                        .status(status)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(errorMessage)
                        .build());
            return;
        }

        try {
            jwtVerifier.verify(token, getRequiredRoleToAccessResource());
        } catch (JwtVerificationException ex) {
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

    private UserRole getRequiredRoleToAccessResource() {
        Class<?> resourceClass = resourceInfo.getResourceClass();
        RequireRoleAccess classAnnotation = resourceClass.getAnnotation(RequireRoleAccess.class);
        UserRole userRole = null;
        if (classAnnotation != null) userRole = classAnnotation.roleRequired();
        Method resourceMethod = resourceInfo.getResourceMethod();
        RequireRoleAccess methodAnnotation = resourceMethod.getAnnotation(RequireRoleAccess.class);
        if (methodAnnotation != null) userRole = methodAnnotation.roleRequired();
        if (userRole != null) return userRole;
        throw new RuntimeException("Oh no! Call the programmer because he missed something");
    }

    public void setResourceInfo(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public void setJwtVerifier(JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }
}
