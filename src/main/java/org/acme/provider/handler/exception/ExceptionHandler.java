package org.acme.provider.handler.exception;

import jakarta.el.MethodNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.acme.enumeration.ResponseEnum;
import org.acme.response.ResponseBase;

@Provider
@Slf4j
public class ExceptionHandler implements ExceptionMapper<Exception> {

    public Response toResponse(IllegalArgumentException e) {
        log.info("--- STACKTRACE ---", e);
        Response.Status httpStatus = Response.Status.BAD_REQUEST;
        ResponseEnum responseEnum = ResponseEnum.BAD_REQUEST;
        return Response
                .status(httpStatus)
                .entity( new ResponseBase(responseEnum) )
                .build();
    }
    public Response toResponse(MethodNotFoundException e) {
        log.info("--- STACKTRACE ---", e);
        Response.Status httpStatus = Response.Status.METHOD_NOT_ALLOWED;
        ResponseEnum responseEnum = ResponseEnum.METHOD_NOT_ALLOWED;
        return Response
                .status(httpStatus)
                .entity( new ResponseBase(responseEnum) )
                .build();
    }
    public Response toResponse(RuntimeException e) {
        log.info("--- STACKTRACE ---", e);
        return this.toResponse(e);
    }

    @Override
    public Response toResponse(Exception e) {
        log.info("--- STACKTRACE ---", e);
        Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
        ResponseEnum responseEnum = ResponseEnum.INTERNAL_SERVER_ERROR;
        return Response
                .status(httpStatus)
                .entity( new ResponseBase(responseEnum) )
                .build();
    }

}
