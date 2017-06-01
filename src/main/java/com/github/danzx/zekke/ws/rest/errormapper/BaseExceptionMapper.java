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

import java.util.Locale;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Base JSON exception mapper.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class BaseExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {

    private HttpHeaders headers;

    /**
     * Creates a new response.
     * 
     * @param status the status
     * @param entity the entity.
     * @return a JSON response.
     */
    protected Response buildJsonResponse(Response.Status status, Object entity) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(entity)
                .build();
    }

    /** @return the client primary locale or Locale.ROOT, never null. */
    protected Locale getClientLocale() {
        return headers.getAcceptableLanguages().stream().findFirst().orElse(Locale.ROOT);
    }

    protected HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(@Context HttpHeaders headers) {
        this.headers = headers;
    }
}
