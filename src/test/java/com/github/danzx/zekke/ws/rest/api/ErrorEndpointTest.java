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

import static java.util.Collections.singletonList;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.danzx.zekke.test.validation.BaseValidationTest;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.junit.Test;

public class ErrorEndpointTest extends BaseValidationTest {

    private static final ErrorEndpoint ENDPOINT = new ErrorEndpoint();

    @Test
    public void shouldResourceNotFoundFailValidationWhenLocalesIsNull() throws Exception {
        Method method = ErrorEndpoint.class.getMethod("resourceNotFound", List.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<ErrorEndpoint>> violations = validator().forExecutables().validateParameters(
                ENDPOINT,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldResourceNotFoundReturnErrorMessage() {
        Response response = ENDPOINT.resourceNotFound(singletonList(Locale.ROOT));

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::getMediaType).containsExactly(Response.Status.NOT_FOUND, MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.getEntity()).isNotNull().isInstanceOf(ErrorMessage.class);

        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity)
            .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
            .containsOnly(404, ErrorMessage.Type.NOT_FOUND, "Resource not found", null);
    }
}
