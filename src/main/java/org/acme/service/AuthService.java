package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.FailureException;
import org.acme.entity.User;
import org.acme.enumeration.ResponseEnum;
import org.acme.repository.UserRepository;
import org.acme.request.LoginRequest;
import org.acme.response.LoginResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public LoginResponse generateAuthResponse(LoginRequest login) {
        User user = checkLogin(login.getUsername());

        String uuid = UUID.randomUUID().toString();
        return LoginResponse.builder()
                .accessToken(jwtTokenService.generateAccessToken(user.getId(), login.getUsername(), uuid, Set.of(user.getRole().getValue())))
                .refreshToken(jwtTokenService.generateRefreshToken(user.getId(), login.getUsername(), uuid, Set.of(user.getRole().getValue())))
                .build();
    }

    private User checkLogin(String email) {
        Optional<User> optFoundUser = Optional.empty();
        List<User> userList = userRepository.findByEmail(email);

        if (!CollectionUtils.isEmpty(userList)) {
            optFoundUser = userList.stream().filter(e -> e.getEmail().equals(email)).findFirst();
        }

        return optFoundUser
                .orElseThrow(() -> new FailureException(Response.Status.NOT_FOUND, ResponseEnum.NOT_FOUND,
                        "NOT FOUND - Wrong username or password"));
    }
}
