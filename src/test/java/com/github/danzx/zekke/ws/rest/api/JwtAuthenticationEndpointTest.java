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

import static java.util.Collections.emptyList;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.SigningKeyHolder;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtFactory;
import com.github.danzx.zekke.service.UserService;
import com.github.danzx.zekke.ws.rest.model.AccessTokenHolder;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage.Type;
import com.github.danzx.zekke.ws.rest.security.BasicAuthorizationHeaderExtractor;

import org.junit.Before;
import org.junit.Test;

public class JwtAuthenticationEndpointTest {

    private static final String JWT_PATTERN = ".+\\..+\\..+";
    private static final JjwtFactory TOKEN_FACTORY;
    private static final BasicAuthorizationHeaderExtractor AUTH_HEADER_EXTRACTOR = new BasicAuthorizationHeaderExtractor();

    static {
        SigningKeyHolder keyHolder = new SigningKeyHolder("keys/test1.key");
        TOKEN_FACTORY = new JjwtFactory(1L, "testIssuer", keyHolder);
    }

    private JwtAuthenticationEndpoint endpoint;
    private UserService userServiceMock;

    @Before
    public void setup() {
        userServiceMock = mock(UserService.class);
        endpoint = new JwtAuthenticationEndpoint(TOKEN_FACTORY, userServiceMock, AUTH_HEADER_EXTRACTOR);
    }

    @Test
    public void shouldAuthenticateAnonymouslyCreateToken() {
        AccessTokenHolder tokenHolder = endpoint.authenticateAnonymously();
        assertThat(tokenHolder).isNotNull();
        assertThat(tokenHolder.getAccessToken()).isNotNull().isNotBlank().matches(JWT_PATTERN);
    }

    @Test
    public void shouldAuthenticateAdminRespondWithInvalidAuthorizationHeaderWhenAuthorizationHeaderIsNotValid() {
        String authorizationHeader = "Not a valid header";
        Response response = endpoint.authenticateAdmin(authorizationHeader, emptyList());

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::hasEntity).containsOnly(Status.BAD_REQUEST, true);
        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity).isNotNull().extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType).containsOnly(400, Type.PARAM_VALIDATION);
    }

    @Test
    public void shouldAuthenticateAdminRespondWithLoginErrorWhenUserIsNotAdmin() {
        String authorizationHeader = "Basic YW55dXNlcjphZG1pbg==";
        Response response = endpoint.authenticateAdmin(authorizationHeader, emptyList());

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::hasEntity).containsOnly(Status.UNAUTHORIZED, true);
        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity).isNotNull().extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType).containsOnly(401, Type.AUTHORIZATION);
    }

    @Test
    public void shouldAuthenticateAdminRespondWithLoginErrorWhenUserIsAdminButNotRegistered() {
        String authorizationHeader = "Basic YWRtaW46YWRtaW4=";
        when(userServiceMock.isAdminRegistered(any(User.class))).thenReturn(false);
        Response response = endpoint.authenticateAdmin(authorizationHeader, emptyList());

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::hasEntity).containsOnly(Status.UNAUTHORIZED, true);
        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity).isNotNull().extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType).containsOnly(401, Type.AUTHORIZATION);
    }

    @Test
    public void shouldAuthenticateAdminRespondWithJwtWhenUserIsARegisteredAdmin() {
        String authorizationHeader = "Basic YWRtaW46YWRtaW4=";
        when(userServiceMock.isAdminRegistered(any(User.class))).thenReturn(true);
        Response response = endpoint.authenticateAdmin(authorizationHeader, emptyList());

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::hasEntity).containsOnly(Status.OK, true);
        AccessTokenHolder tokenHolder = (AccessTokenHolder) response.getEntity();
        assertThat(tokenHolder).isNotNull();
        assertThat(tokenHolder.getAccessToken()).isNotNull().isNotBlank().matches(JWT_PATTERN);
    }
}
