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

import static java.util.Collections.emptyList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.SigningKeyHolder;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtFactory;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtVerifier;
import com.github.danzx.zekke.test.mockito.BaseMockitoTest;
import com.github.danzx.zekke.ws.rest.security.RequireRoleAccess;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class JwtAuthenticationFilterTest extends BaseMockitoTest {

    private static final SigningKeyHolder KEY_HOLDER = new SigningKeyHolder("keys/test1.key");
    private static final String ISSUER = "testIssuer";
    private static final JjwtVerifier VERIFIER = new JjwtVerifier(ISSUER, KEY_HOLDER);
    private static final JjwtFactory TOKEN_FACTORY = new JjwtFactory(1L, ISSUER, KEY_HOLDER);
    
    private @Mock ContainerRequestContext requestContext;
    private @Mock ResourceInfo resourceInfo;
    
    private @InjectMocks JwtAuthenticationFilter filter;

    @Before
    public void setUp() {
        filter.setJwtVerifier(VERIFIER);
    }

    @Test
    public void shouldNotAbortRequestWithAnnotationAtClassLevel() throws Exception {
        String token = TOKEN_FACTORY.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(new Answer<Class<?>>() {
            @Override public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return AnnotatedClass.class;
            }
        });
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClass.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void shouldNotAbortRequestWithAnnotationAtClassLevelAndAnnotatedMethod() throws Exception {
        String token = TOKEN_FACTORY.newToken(User.Role.ADMIN);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(new Answer<Class<?>>() {
            @Override public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return AnnotatedClassWithAnnotatedMethod.class;
            }
        });
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClassWithAnnotatedMethod.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void shouldFilterThrowRuntimeExceptionWhenUsingNotAnnotatedClass() throws Exception {
        String token = TOKEN_FACTORY.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(new Answer<Class<?>>() {
            @Override public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return NotAnnotatedClass.class;
            }
        });
        when(resourceInfo.getResourceMethod()).thenReturn(NotAnnotatedClass.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);

        assertThatThrownBy(() -> filter.filter(requestContext)).isInstanceOf(RuntimeException.class).hasMessage("Oh no! Call the programmer because he missed something");
    }

    @Test
    public void shouldAbortRequestIfTokenIsNotFound() throws Exception {
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        filter.filter(requestContext);

        verify(requestContext).abortWith(any());
    }

    @Test
    public void shouldAbortRequestIfTokenIsInvalid() throws Exception {
        String token = TOKEN_FACTORY.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(new Answer<Class<?>>() {
            @Override public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return AnnotatedClassWithAnnotatedMethod.class;
            }
        });
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClassWithAnnotatedMethod.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext).abortWith(any());
    }

    @RequireRoleAccess
    private static class AnnotatedClass { 
        @SuppressWarnings("unused") public void foo() { }
    }

    @RequireRoleAccess
    private static class AnnotatedClassWithAnnotatedMethod { 
        
        @RequireRoleAccess(roleRequired = User.Role.ADMIN)
        public void foo() { }
    }

    @SuppressWarnings("unused") private static class NotAnnotatedClass {
        public void foo() { }
    }
}
