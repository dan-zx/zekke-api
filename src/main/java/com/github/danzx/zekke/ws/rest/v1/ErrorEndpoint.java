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
package com.github.danzx.zekke.ws.rest.v1;

import static com.github.danzx.zekke.ws.rest.ApiVersions.V_1;

import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.model.ErrorMessage;

import org.springframework.stereotype.Component;

/**
 * Error enpoint.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@Path(V_1 + "/errors")
public class ErrorEndpoint {

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    /**
     * 404 Not Found endpoint
     * 
     * @param locales the client locales
     * @return a JSON response associated to the error 404 Not Found.
     */
    @GET
    @Path("/404")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resourceNotFound(@NotNull @HeaderParam("Accept-Language") List<Locale> locales) {
        Locale clientLocale = locales.stream().findFirst().orElse(Locale.ROOT);
        Response.Status status = Response.Status.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.RESOURCE_NOT_FOUND)
                .detailMessage(messageSource.getMessage("resource.not.found.error", clientLocale))
                .build();
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}
