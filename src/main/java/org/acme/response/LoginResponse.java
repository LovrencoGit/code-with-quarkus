package org.acme.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class LoginResponse extends AuthResponse {
//    private DTOUser dtoUser; TODO capire se lo posso usare secondo Loris
}
