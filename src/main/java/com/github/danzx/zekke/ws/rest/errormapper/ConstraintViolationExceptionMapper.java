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
import com.github.danzx.zekke.ws.rest.common.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
