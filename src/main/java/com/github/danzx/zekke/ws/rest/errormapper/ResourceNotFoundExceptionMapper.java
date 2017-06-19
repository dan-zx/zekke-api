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

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps NotFoundException to JSON Responses.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class ResourceNotFoundExceptionMapper extends BaseExceptionMapper<NotFoundException> {

    private static final Logger log = LoggerFactory.getLogger(ResourceNotFoundExceptionMapper.class);

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public Response toResponse(NotFoundException exception) {
        log.error("Resource not found", exception);
        Response.Status status = Response.Status.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.RESOURCE_NOT_FOUND)
                .detailMessage(messageSource.getMessage("resource.not.found.error", getClientLocale()))
                .build();
        return buildJsonResponse(status, errorMessage);
    }
}
