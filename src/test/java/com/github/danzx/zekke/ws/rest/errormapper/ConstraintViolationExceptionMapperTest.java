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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.test.mockito.BaseMockitoValidationTest;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ConstraintViolationExceptionMapperTest extends BaseMockitoValidationTest {

    private @Mock HttpHeaders headers;

    private @InjectMocks ConstraintViolationExceptionMapper mapper;

    @Test
    public void shouldHeadersBeNotNull() {
        assertThat(mapper.getHeaders()).isNotNull();
    }

    @Test
    public void shouldMapperRespondWithErrorMessageWithSimpleParameter() throws Exception {
        Method method = ValidatedObject.class.getMethod("doStuff", Object.class);
        Object[] parameterValues = { null };
        Set<ConstraintViolation<ValidatedObject>> violations = validator().forExecutables().validateParameters(
                new ValidatedObject(),
                method,
                parameterValues
        );
        Response response = mapper.toResponse(new ConstraintViolationException(violations));

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::getMediaType).containsExactly(Response.Status.BAD_REQUEST, MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.getEntity()).isNotNull().isInstanceOf(ErrorMessage.class);

        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity)
            .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
            .containsOnly(400, ErrorMessage.Type.PARAM_VALIDATION, "Parameter validation failed", singletonMap("arg0", "may not be null"));
    }

    @Test
    public void shouldMapperRespondWithErrorMessageWithComplexParameter() throws Exception {
        Method method = ValidatedObject.class.getMethod("doStuff2", BoundingBox.class);
        Object[] parameterValues = { BoundingBox.ofBottomTop(Coordinates.ofLatLng(123132.243, 5d), null) };
        Set<ConstraintViolation<ValidatedObject>> violations = validator().forExecutables().validateParameters(
                new ValidatedObject(),
                method,
                parameterValues
        );
        Response response = mapper.toResponse(new ConstraintViolationException(violations));

        assertThat(response).isNotNull().extracting(Response::getStatusInfo, Response::getMediaType).containsExactly(Response.Status.BAD_REQUEST, MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.getEntity()).isNotNull().isInstanceOf(ErrorMessage.class);

        Map<String, String> paramErrors = new HashMap<>();
        paramErrors.put("bottomCoordinates.latitude", "must be between -90.0 and 90.0");
        paramErrors.put("topCoordinates", "may not be null");
        ErrorMessage entity = (ErrorMessage) response.getEntity();
        assertThat(entity)
            .extracting(ErrorMessage::getStatusCode, ErrorMessage::getErrorType, ErrorMessage::getErrorDetail, ErrorMessage::getParamErrors)
            .containsOnly(400, ErrorMessage.Type.PARAM_VALIDATION, "Parameter validation failed", paramErrors);
    }

    private static class ValidatedObject {
        
        @SuppressWarnings("unused")
        public void doStuff(@NotNull Object arg0) { }
        
        @SuppressWarnings("unused")
        public void doStuff2(@NotNull @Valid BoundingBox arg0) { }
    }
}
