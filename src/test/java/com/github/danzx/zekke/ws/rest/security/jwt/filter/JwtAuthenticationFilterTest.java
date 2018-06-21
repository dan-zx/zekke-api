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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.JwtFactory;
import com.github.danzx.zekke.security.jwt.JwtSettings;
import com.github.danzx.zekke.security.jwt.JwtVerifier;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtFactory;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtVerifier;
import com.github.danzx.zekke.test.spring.BaseMockitoSpringTest;
import com.github.danzx.zekke.ws.rest.security.BearerAuthorizationHeaderExtractor;
import com.github.danzx.zekke.ws.rest.security.RequireRoleAccess;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = JwtSettings.class)
@TestPropertySource(locations = "classpath:test.properties")
public class JwtAuthenticationFilterTest extends BaseMockitoSpringTest {

    private static final BearerAuthorizationHeaderExtractor AUTH_HEADER_EXTRACTOR = new BearerAuthorizationHeaderExtractor();

    private @Inject JwtSettings jwtSettings;
    private @Mock ContainerRequestContext requestContext;
    private @Mock ResourceInfo resourceInfo;
    private @InjectMocks JwtAuthenticationFilter filter;

    private JwtVerifier jwtVerifier;
    private JwtFactory jwtFactory;

    @Before
    public void setUp() {
        assertThat(jwtSettings).isNotNull();
        jwtVerifier = new JjwtVerifier(jwtSettings);
        jwtFactory = new JjwtFactory(jwtSettings);
        filter.setJwtVerifier(jwtVerifier);
        filter.setAuthorizationHeaderExtractor(AUTH_HEADER_EXTRACTOR);
    }

    @Test
    public void shouldNotAbortRequestWithAnnotationAtClassLevel() throws Exception {
        String token = jwtFactory.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(invocation -> AnnotatedClass.class);
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClass.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void shouldNotAbortRequestWithAnnotationAtClassLevelAndAnnotatedMethod() throws Exception {
        String token = jwtFactory.newToken(User.Role.ADMIN);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(invocation -> AnnotatedClassWithAnnotatedMethod.class);
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClassWithAnnotatedMethod.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void shouldFilterThrowUnsupportedOperationExceptionWhenUsingNotAnnotatedClassAndNotAnnotatedMethod() throws Exception {
        String token = jwtFactory.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(invocation -> NotAnnotatedClass.class);
        when(resourceInfo.getResourceMethod()).thenReturn(NotAnnotatedClass.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);

        assertThatThrownBy(() -> filter.filter(requestContext)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void shouldAbortRequestIfTokenIsNotFound() {
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        filter.filter(requestContext);

        verify(requestContext).abortWith(any());
    }

    @Test
    public void shouldAbortRequestIfTokenIsInvalid() throws Exception {
        String token = jwtFactory.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(invocation -> AnnotatedClassWithAnnotatedMethod.class);
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClassWithAnnotatedMethod.class.getMethod("foo"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext).abortWith(any());
    }

    @Test
    public void shouldNotAbortRequestIfTokenAdminWhenRequiredIsAnonymous() throws Exception {
        String token = jwtFactory.newToken(User.Role.ADMIN);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(invocation -> AnnotatedClassWithAnnotatedMethod.class);
        when(resourceInfo.getResourceMethod()).thenReturn(AnnotatedClassWithAnnotatedMethod.class.getMethod("bar"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void shouldNotAbortRequestWithNotAnnotatedClassAndAnnotationAtMethodLevel() throws Exception {
        String token = jwtFactory.newToken(User.Role.ANONYMOUS);
        String headerInfo = "Bearer " + token;

        when(resourceInfo.getResourceClass()).thenAnswer(invocation -> NotAnnotatedClass.class);
        when(resourceInfo.getResourceMethod()).thenReturn(NotAnnotatedClass.class.getMethod("bar"));
        when(requestContext.getAcceptableLanguages()).thenReturn(emptyList());
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(headerInfo);
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @RequireRoleAccess
    private static class AnnotatedClass { 
        @SuppressWarnings("unused") public void foo() { }
    }

    @RequireRoleAccess
    private static class AnnotatedClassWithAnnotatedMethod { 
        
        @RequireRoleAccess(roleRequired = User.Role.ADMIN)
        public void foo() { }

        @SuppressWarnings("unused")
        public void bar() { }
    }

    private static class NotAnnotatedClass {
        public void foo() { }

        @RequireRoleAccess
        @SuppressWarnings("unused")
        public void bar() { }
    }
}
