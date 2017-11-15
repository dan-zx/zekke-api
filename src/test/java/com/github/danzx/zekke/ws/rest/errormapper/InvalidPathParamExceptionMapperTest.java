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
package com.github.danzx.zekke.ws.rest.errormapper;

import static java.util.Collections.singletonMap;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.danzx.zekke.test.mockito.BaseMockitoTest;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.glassfish.jersey.server.ParamException;

import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

public class InvalidPathParamExceptionMapperTest extends BaseMockitoTest {

    private @Mock HttpHeaders headers;

    private @InjectMocks InvalidPathParamExceptionMapper mapper;

    @Test
    public void shouldHeadersBeNotNull() {
        assertThat(mapper.getHeaders()).isNotNull();
    }

    @Test
    public void shouldMapperRespondWithErrorMessage() {
        ParamException.PathParamException exception = new ParamException.PathParamException(new IllegalArgumentException("invalid"), "arg0", null);
        Response response = mapper.toResponse(exception);

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::getMediaType).containsExactly(Response.Status.BAD_REQUEST, MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.getEntity()).isNotNull().isInstanceOf(ErrorMessage.class);

        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity)
            .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
            .containsOnly(400, ErrorMessage.Type.PARAM_VALIDATION, "Parameter validation failed", singletonMap("arg0", "invalid"));
    }
}
