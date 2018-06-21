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
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import static com.github.danzx.zekke.test.assertion.ProjectAssertions.assertThat;

import javax.ws.rs.core.Response;

import com.github.danzx.zekke.test.jersey.BaseJerseyTest;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.junit.Test;

public class ErrorEndpointTest extends BaseJerseyTest {

    @Test
    public void shouldResourceNotFoundReturnErrorMessage() {
        Response response = target("v1/errors/404")
                .request()
                .accept(APPLICATION_JSON_TYPE)
                .get();

        assertThat(response)
                .produced(APPLICATION_JSON_TYPE)
                .respondedWith(NOT_FOUND)
                .extractingEntityAs(ErrorMessage.class)
                    .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
                    .containsOnly(404, ErrorMessage.Type.NOT_FOUND, "Resource not found", null);
    }
}
