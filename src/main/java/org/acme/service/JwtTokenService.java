package org.acme.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.FailureException;
import org.acme.constants.AuthConstants;
import org.acme.enumeration.ResponseEnum;
import org.acme.request.RefreshRequest;
import org.acme.response.AuthResponse;
import org.acme.service.models.JwtClaims;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class JwtTokenService {
    private final String refreshSignKey;
    private final String refreshPublicKey;
    private final String accessPublicKey;
    private final int accessTokenDuration;
    private final int refreshTokenDuration;

    private final HttpServerRequest request;

    @Inject
    public JwtTokenService(@ConfigProperty(name = "authentication.jwt.refresh.signkey.location") String refreshSignKey,
                       @ConfigProperty(name = "authentication.jwt.refresh.publickey.location") String refreshPublicKey,
                       @ConfigProperty(name = "mp.jwt.verify.publickey.location") String accessPublicKey,
                       @ConfigProperty(name = "authentication.jwt.refresh.token.duration") int accessTokenDuration,
                       @ConfigProperty(name = "authentication.jwt.access.token.duration") int refreshTokenDuration,
                       @Context HttpServerRequest request) {
        this.refreshSignKey = refreshSignKey;
        this.refreshPublicKey = refreshPublicKey;
        this.accessPublicKey = accessPublicKey;
        this.accessTokenDuration = accessTokenDuration;
        this.refreshTokenDuration = refreshTokenDuration;
        this.request = request;
    }

    public String generateAccessToken(Long userId, String username, String uuid) {
        return addJWTClaims(userId, username, uuid)
                .expiresIn(accessTokenDuration)
                .sign();
    }

    public String generateRefreshToken(Long userId, String username, String uuid) {
        return addJWTClaims(userId, username, uuid)
                .expiresIn(refreshTokenDuration)
                .jws()
                .sign(getPrivateRefreshKey());
    }

    public AuthResponse refreshToken(RefreshRequest refreshRequest) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        String refreshToken = refreshRequest.getRefreshToken();

        try {
            // checking if access token and refresh token are signed correctly. It does not matter if they are expired
            JwtClaims accessTokenClaims = validateTokenAndRetrieveClaims(accessToken, accessPublicKey);
            JwtClaims refreshTokenClaims = validateTokenAndRetrieveClaims(refreshToken, refreshPublicKey);

            if (Objects.equals(accessTokenClaims.getUuid(), refreshTokenClaims.getUuid())) {
                if (LocalDateTime.now(ZoneOffset.UTC).isAfter(accessTokenClaims.getExpirationDate())) {
                    // if the tokens uuid matches and the access token is expired the method generates a new token pair
                    String username = accessTokenClaims.getUsername();
                    Long userId = accessTokenClaims.getUserId();
                    String uuid = UUID.randomUUID().toString();

                    accessToken = generateAccessToken(userId, username, uuid);
                    refreshToken = generateRefreshToken(userId, username, uuid);
                }
            } else {
                // if the access token uuid does not match the refresh token jwt the method will throw an exception
                throw new FailureException(Response.Status.UNAUTHORIZED, ResponseEnum.UNAUTHORIZED,
                        "UNAUTHORIZED - The pair access token-resource token does not match");
            }
        } catch (JsonProcessingException ex) {
            log.error("STACK TRACE:\n", ex);
            throw new FailureException(Response.Status.UNAUTHORIZED, ResponseEnum.UNAUTHORIZED,
                    "UNAUTHORIZED - The refresh or the access token does not match the correct signature");
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private JwtClaimsBuilder addJWTClaims(Long userId, String username, String uuid) {
        return Jwt
                .upn(username)
                .claim(AuthConstants.USER_ID_CLAIM_NAME, userId)
                .claim(AuthConstants.USERNAME_CLAIM_NAME, username)
                .claim(AuthConstants.UUID_CLAIM_NAME, uuid);
    }

    private String removeBeginEnd(String privateKey) {
        return privateKey.replaceAll("-----BEGIN(.*)-----", "")
                .replaceAll("-----END(.*)-----", "")
                .replaceAll("\\s+", "");
    }

    private PrivateKey getPrivateRefreshKey() {
        Path privateKeyPath = Paths.get(refreshSignKey);
        try {
            String privateKey = Files.readString(privateKeyPath);

            byte[] decodedPrivateKey = Base64.getDecoder().decode(removeBeginEnd(privateKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("STACKTRACE:\n", ex);
            throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                    "INTERNAL SERVER ERROR - Error occurred while generating refresh sign key");
        }
    }

    private RSAPublicKey getPublicKey(String pathToPublicKey) {
        Path publicKeyPath = Paths.get(pathToPublicKey);
        try {

            String publicKey = Files.readString(publicKeyPath);
            byte[] decodedPublicKey = Base64.getDecoder().decode(removeBeginEnd(publicKey));

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("STACKTRACE:\n", ex);
            throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                    "INTERNAL SERVER ERROR - Error occurred while generating refresh public key");
        }
    }

    private JwtClaims validateTokenAndRetrieveClaims(String token, String publicKeyLocation) throws JsonProcessingException {
        String payload;

        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            DecodedJWT decodedJWT = verifyToken(token, getPublicKey(publicKeyLocation));
            payload = decodedJWT.getPayload();
        } catch (SignatureVerificationException ex) {
            log.error("STACK TRACE:\n", ex);
            throw new FailureException(Response.Status.UNAUTHORIZED, ResponseEnum.UNAUTHORIZED,
                    "UNAUTHORIZED - The refresh or the access token does not match the correct signature");
        } catch (TokenExpiredException ex) {
            log.info("Token expired - Manual decode to extract claims");
            String[] splittedToken = token.split("\\.");
            payload = splittedToken[1];
        }

        String decodedClaims = new String(decoder.decode(payload), StandardCharsets.UTF_8);
        return mapper.readValue(decodedClaims, JwtClaims.class);
    }

    private DecodedJWT verifyToken(String token, RSAPublicKey publicKey) {
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }
}
