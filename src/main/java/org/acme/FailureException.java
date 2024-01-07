package org.acme;

import jakarta.ws.rs.core.Response;
import lombok.*;
import org.acme.enumeration.ResponseEnum;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FailureException extends RuntimeException {

    private Exception exception;
    private Response.Status httpStatus;
    private ResponseEnum responseEnum;
    private String details;


    public FailureException(Response.Status httpStatus, ResponseEnum responseEnum) {
        this(null, httpStatus, responseEnum, null);
        this.details = responseEnum.defaultMessage;
    }
    public FailureException(Response.Status httpStatus, ResponseEnum responseEnum, String details) {
        this(null, httpStatus, responseEnum, details);
    }

    /**************************************************/

    public FailureException(Exception e, ResponseEnum responseEnum, String details){
        this(e, null, responseEnum, details);
        this.httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
        this.responseEnum = responseEnum;
    }
    public FailureException(Exception e, Response.Status httpStatus, ResponseEnum responseEnum){
        this(e, httpStatus, responseEnum, null);
        this.details = e.getMessage();
        this.responseEnum = responseEnum;
    }
    public FailureException(Exception e, ResponseEnum responseEnum){
        this(e, null, responseEnum, null);
        this.details = e.getMessage();
        this.responseEnum = responseEnum;
        this.httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    }

    public FailureException(Exception e){
        this(e, null, null, null);
        this.details = e.getMessage();
        this.httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
        this.responseEnum = ResponseEnum.INTERNAL_SERVER_ERROR;
    }

}

