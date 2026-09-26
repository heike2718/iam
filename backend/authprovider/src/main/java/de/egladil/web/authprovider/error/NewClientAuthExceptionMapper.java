package de.egladil.web.authprovider.error;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class NewClientAuthExceptionMapper implements ExceptionMapper<NewClientAuthException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewClientAuthExceptionMapper.class);

    @Override
    public Response toResponse(NewClientAuthException exception) {
        LOGGER.error(exception.getMessage(), exception);

        return Response.status(Status.UNAUTHORIZED).build();
    }

}
