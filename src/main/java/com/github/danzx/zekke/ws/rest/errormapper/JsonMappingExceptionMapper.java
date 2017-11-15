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

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps JSON mapping exception to JSON Responses.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class JsonMappingExceptionMapper extends BaseExceptionMapper<JsonMappingException>  {

    private static final Logger log = LoggerFactory.getLogger(JsonMappingExceptionMapper.class);

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public Response toResponse(JsonMappingException exception) {
        log.error("JSON mapping error", exception);
        Response.Status status = Response.Status.BAD_REQUEST;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.PARAM_VALIDATION)
                .detailMessage(messageSource.getMessage("json.mapping.error", getClientLocale()))
                .build();
        return buildJsonResponse(status, errorMessage);
    }
}
