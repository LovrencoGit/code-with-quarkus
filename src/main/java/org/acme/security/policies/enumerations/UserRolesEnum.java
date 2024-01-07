package org.acme.security.policies.enumerations;

import io.vertx.core.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum UserRolesEnum {
    ADMIN("ADMIN", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)),
    MANAGER("MANAGER", List.of(HttpMethod.GET, HttpMethod.PATCH)),
    CONSULTANT("CONSULTANT", List.of(HttpMethod.GET));

    private final String value;
    private final List<HttpMethod> allowedOperations;

    public boolean isAuthorized(HttpMethod operationMethod) {
        return allowedOperations.contains(operationMethod);
    }

    public static UserRolesEnum fromValue(String searchedValue) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.value, searchedValue))
                .findFirst()
                .orElse(null);
    }
}
