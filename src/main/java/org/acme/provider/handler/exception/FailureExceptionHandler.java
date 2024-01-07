package org.acme.provider.handler.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.acme.FailureException;
import org.acme.response.ResponseBase;

@Provider
@Slf4j
public class FailureExceptionHandler implements ExceptionMapper<FailureException> {
    @Override
    public Response toResponse(FailureException e) {
        Response.Status httpStatus = e.getHttpStatus();
        ResponseBase response = new ResponseBase(e.getResponseEnum());
        Exception exception = e.getException();
        log.error("[FAILURE EXCEPTION] - {} - details: {}", response, e.getDetails());
        if(exception != null)
            log.info("--- STACKTRACE ---", exception);
        return Response
                .status(httpStatus)
                .entity(response)
                .build();
    }

}
