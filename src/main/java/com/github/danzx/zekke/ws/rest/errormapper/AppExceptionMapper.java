package com.github.danzx.zekke.ws.rest.errormapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.danzx.zekke.exception.AppException;
import com.github.danzx.zekke.ws.rest.common.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
