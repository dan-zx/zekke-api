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

import static java.util.stream.Collectors.joining;

import java.util.Locale;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path.Node;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps ConstraintViolationException to JSON Responses.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class ConstraintViolationExceptionMapper extends BaseExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class); 

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        LOGGER.error("Failed validation", exception);
        Response.Status status = Response.Status.BAD_REQUEST;
        Locale clientLocale = getClientLocale();
        ErrorMessage.Builder errorMessageBuilder = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.PARAM_VALIDATION)
                .detailMessage(messageSource.getMessage("param.validation.error", clientLocale));
        exception.getConstraintViolations().stream()
            .forEach(violation -> addParamError(violation, errorMessageBuilder));

        return buildJsonResponse(status, errorMessageBuilder.build());
    }

    private void addParamError(ConstraintViolation<?> constraintViolation, ErrorMessage.Builder errorMessageBuilder) {
        String property = StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false)
            .filter(node -> node.getKind() == ElementKind.PROPERTY)
            .map(Node::toString)
            .collect(joining("."));
        errorMessageBuilder.addParamError(property, constraintViolation.getMessage());
    }
}
