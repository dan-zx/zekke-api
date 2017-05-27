package com.github.danzx.zekke.ws.rest.errormapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;
import com.github.danzx.zekke.ws.rest.common.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GenericExceptionMapper extends BaseExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionMapper.class);

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.error("Unexpected error", exception);
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .statusCode(status.getStatusCode())
                .type(ErrorMessage.Type.OTHER)
                .detailMessage(messageSource.getMessage("unexpected.error", getClientLocale(), exception.getMessage()))
                .build();
        return buildJsonResponse(status, errorMessage);
    }
}
