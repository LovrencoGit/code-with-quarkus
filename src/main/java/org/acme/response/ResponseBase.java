package org.acme.response;

import lombok.*;
import org.acme.enumeration.ResponseEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseBase {

    private int code;
    private String message;

    public ResponseBase(ResponseEnum responseEnum){
        this.code = responseEnum.code;
        this.message = responseEnum.defaultMessage;
    }

}
