package com.github.danzx.zekke.ws.rest.errormapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.common.ErrorMessage;

@Provider
public class ResourceNotFoundExceptionMapper extends BaseExceptionMapper<NotFoundException> {

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public Response toResponse(NotFoundException exception) {
        Response.Status status = Response.Status.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.RESOURCE_NOT_FOUND)
                .detailMessage(messageSource.getMessage("resource.not.found.error", getClientLocale()))
                .build();
        return buildJsonResponse(status, errorMessage);
    }
}
