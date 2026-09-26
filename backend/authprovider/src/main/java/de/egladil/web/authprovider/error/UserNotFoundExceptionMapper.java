package de.egladil.web.authprovider.error;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * UserNotFoundExceptionMapper.
 */
@Provider
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {

    @Override
    public Response toResponse(UserNotFoundException exception) {
        return Response.status(Status.NOT_FOUND).build();
    }

}
