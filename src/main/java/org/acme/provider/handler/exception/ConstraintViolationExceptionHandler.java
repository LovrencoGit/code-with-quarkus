package org.acme.provider.handler.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.acme.FailureException;
import org.acme.enumeration.ResponseEnum;
import org.acme.response.ResponseBase;

@Provider
@Slf4j
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        Response.Status httpStatus = Response.Status.BAD_REQUEST;
        int code = ResponseEnum.BAD_REQUEST.code;
        String message = e.getLocalizedMessage();
        ResponseBase response = ResponseBase.builder().code(code).message(message).build();
        log.info("[ConstraintViolationException] - {}", e.getConstraintViolations());
        return Response
                .status(httpStatus)
                .entity(response)
                .build();
    }

}
