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

import com.github.danzx.zekke.exception.AppException;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps AppException to JSON Responses.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
public class AppExceptionMapper extends BaseExceptionMapper<AppException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionMapper.class);

    @Override
    public Response toResponse(AppException exception) {
        LOGGER.error("Application failure", exception);
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.SERVER_ERROR)
                .detailMessage(exception.getMessage(getClientLocale()))
                .build();
        return buildJsonResponse(status, errorMessage);
    }
}
