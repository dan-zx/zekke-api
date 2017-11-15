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

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonMappingException;

import com.github.danzx.zekke.test.mockito.BaseMockitoTest;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

public class JsonMappingExceptionMapperTest extends BaseMockitoTest {

    private @Mock HttpHeaders headers;

    private @InjectMocks JsonMappingExceptionMapper mapper;

    @Test
    public void shouldHeadersBeNotNull() {
        assertThat(mapper.getHeaders()).isNotNull();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void shouldMapperRespondWithErrorMessage() {
        Response response = mapper.toResponse(new JsonMappingException("Unexpected error"));

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::getMediaType).containsExactly(Response.Status.BAD_REQUEST, MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.getEntity()).isNotNull().isInstanceOf(ErrorMessage.class);

        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity)
            .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
            .containsOnly(400, ErrorMessage.Type.PARAM_VALIDATION, "Malformed JSON payload", null);
    }
}
