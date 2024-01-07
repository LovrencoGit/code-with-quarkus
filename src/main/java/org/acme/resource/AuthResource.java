package org.acme.resource;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.request.LoginRequest;
import org.acme.request.RefreshRequest;
import org.acme.response.AuthResponse;
import org.acme.response.LoginResponse;
import org.acme.service.AuthService;
import org.acme.service.JwtTokenService;

@Slf4j
@Path("/auth/login")
@AllArgsConstructor
public class AuthResource {
    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    @POST
    public Response login(LoginRequest login) {
        LoginResponse loginResponse = authService.generateAuthResponse(login);
        return Response.ok(loginResponse).build();
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(RefreshRequest refreshRequest) {
        AuthResponse authResponse = jwtTokenService.refreshToken(refreshRequest);
        return Response.ok(authResponse).build();
    }
}
