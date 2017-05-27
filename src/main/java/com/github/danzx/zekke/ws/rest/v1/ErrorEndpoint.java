package com.github.danzx.zekke.ws.rest.v1;

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
import com.github.danzx.zekke.ws.rest.common.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
@Path(V_1 + "/errors")
public class ErrorEndpoint {

    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    @GET
    @Path("/404")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resourceNotFound(@HeaderParam("Accept-Language") List<Locale> locales) {
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
