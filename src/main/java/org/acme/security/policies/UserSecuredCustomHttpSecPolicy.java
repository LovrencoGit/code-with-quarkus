package org.acme.security.policies;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityPolicy;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.security.policies.enumerations.UserRolesEnum;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class UserSecuredCustomHttpSecPolicy implements HttpSecurityPolicy {
    private final JWTParser jwtParser;

    @Override
    public Uni<CheckResult> checkPermission(RoutingContext request, Uni<SecurityIdentity> identity,
                                            AuthorizationRequestContext requestContext) {
        boolean authorized = false;
        String token = request.request().getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        HttpMethod reqMethod = request.request().method();

        try {
            JsonWebToken jwt = jwtParser.parse(token);

            Set<String> groups = Optional.ofNullable(jwt.getGroups()).orElse(new HashSet<>());
            for (String role : groups) {
                UserRolesEnum usrRole = UserRolesEnum.fromValue(role);
                if(usrRole != null) {
                    authorized = authorized || usrRole.isAuthorized(reqMethod);
                }
            }
        } catch (ParseException e) {
            log.error("Error while parsing: \n", e);
        }
        return Uni.createFrom().item(authorized ? CheckResult.PERMIT : CheckResult.DENY);
    }

    @Override
    public String name() {
        return "user-secured-custom-policy";
    }
}
