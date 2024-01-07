package org.acme.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseEnum {

    OK(0, "OK"),

    BAD_REQUEST(            400, "BAD REQUEST"),
    UNAUTHORIZED(           401, "UNAUTHORIZED"),
    FORBIDDEN(              403, "FORBIDDEN"),
    NOT_FOUND(              404, "RESOURCE NOT FOUND"),
    METHOD_NOT_ALLOWED(     405, "METHOD NOT ALLOWED"),
    INTERNAL_SERVER_ERROR(  500, "UNEXPECTED ERROR"),

    MY_CUSTOM_ERROR_1(      1001, "My custom error"), //to edit
    MY_CUSTOM_ERROR_2(      1002, "My custom error"), //to edit
    MY_CUSTOM_ERROR_3(      1003, "My custom error"), //to edit
    MY_CUSTOM_ERROR_4(      1004, "My custom error"), //to edit
    MY_CUSTOM_ERROR_5(      1005, "My custom error"); //to edit



    public int code;
    public String defaultMessage;

}
