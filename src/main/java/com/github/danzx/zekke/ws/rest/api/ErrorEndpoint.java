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

import static com.github.danzx.zekke.ws.rest.ApiVersions.V_1;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * Error enpoint.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@Path(V_1 + "/errors")
public class ErrorEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ErrorEndpoint.class);

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    /**
     * 404 Not Found endpoint
     * 
     * @param clientLocales "Accept-Language" header.
     * @return a JSON response associated to the error 404 Not Found.
     */
    @GET
    @Path("/404")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resourceNotFound(@HeaderParam("Accept-Language") List<Locale> clientLocales) {
        log.info("GET /errors/404 -- Accept-Languages={}", clientLocales);
        Locale clientLocale = clientLocales.stream().findFirst().orElse(Locale.ROOT);
        Response.Status status = Response.Status.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.NOT_FOUND)
                .detailMessage(messageSource.getMessage("resource.not.found.error", clientLocale))
                .build();
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}
