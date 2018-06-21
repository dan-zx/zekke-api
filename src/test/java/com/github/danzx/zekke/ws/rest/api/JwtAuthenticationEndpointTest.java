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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import static com.github.danzx.zekke.test.assertion.ProjectAssertions.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import com.github.danzx.zekke.domain.User.Role;
import com.github.danzx.zekke.test.jersey.BaseJerseyTest;
import com.github.danzx.zekke.ws.rest.model.AccessTokenHolder;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class JwtAuthenticationEndpointTest extends BaseJerseyTest {

    @Test
    public void shouldAuthenticateAnonymouslyReturnTokenWithNoError() {
        String expectedToken = "mockToken";
        doReturn(expectedToken).when(getJwtFactory()).newToken(any());

        Response response = target("v1/authentication/jwt/anonymous")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        verify(getJwtFactory()).newToken(Role.ANONYMOUS);
        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAs(AccessTokenHolder.class)
                    .extracting(AccessTokenHolder::getAccessToken)
                    .containsOnly(expectedToken);
    }

    @Test
    public void shouldAuthenticateAdminReturnTokenWhenLoginIsValid() {
        String expectedToken = "mockToken";
        doReturn(expectedToken).when(getJwtFactory()).newToken(any());
        when(getMockUserService().isAdminRegistered(any())).thenReturn(true);

        Response response = target("v1/authentication/jwt/admin")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .get();

        verify(getJwtFactory()).newToken(Role.ADMIN);
        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(OK)
                .extractingEntityAs(AccessTokenHolder.class)
                    .extracting(AccessTokenHolder::getAccessToken)
                    .containsOnly(expectedToken);
    }

    @Test
    @Parameters(method = "invalidHeaders")
    public void shouldAuthenticateAdminRespondWithError400WhenAuthorizationHeaderIsNotValid(String headerValue, ErrorMessage expectedErrorMessage) {
        Response response = target("v1/authentication/jwt/admin")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .header("Authorization", headerValue)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(BAD_REQUEST)
                .extractingEntityAs(ErrorMessage.class)
                    .isEqualTo(expectedErrorMessage);
    }

    @Test
    @Parameters(method = "invalidLogins")
    public void shouldAuthenticateAdminRespondWithError401WhenLoginIsNotValid(String headerValue) {
        when(getMockUserService().isAdminRegistered(any())).thenReturn(false);
        Response response = target("v1/authentication/jwt/admin")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .header("Authorization", headerValue)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(UNAUTHORIZED)
                .extractingEntityAs(ErrorMessage.class)
                    .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
                    .containsOnly(401, ErrorMessage.Type.AUTHORIZATION, "Unauthorized: Invalid user/password combination", null);
    }

    public Object[] invalidHeaders() {
        return new Object[][] {
            {null, new ErrorMessage.Builder().statusCode(400).type(ErrorMessage.Type.PARAM_VALIDATION).detailMessage("Parameter validation failed").addParamError("arg0", "may not be null").build()},
            {"Not a valid header", new ErrorMessage.Builder().statusCode(400).type(ErrorMessage.Type.PARAM_VALIDATION).detailMessage("Authorization header bad format. Expected: Basic [Base64<username:password>]").build()},
        };
    }

    public Object[] invalidLogins() {
        return new Object[][] {
            {"Basic YW55dXNlcjphZG1pbg=="}, // anyuser:admin
            {"Basic YW5vbnltb3VzOmFkbWlu"}, // anonymous:admin
            {"Basic YWRtaW46YWRtaW4="}, // admin:admin
        };
    }
}
