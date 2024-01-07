package org.acme.service.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtClaims {
    private String upn;
    private long userId;
    private String username;
    private String uuid;
    private LocalDateTime issuedAt;
    private LocalDateTime expirationDate;
    private String jti;

    @JsonProperty("exp")
    public void setExpirationDate(long exp) {
        expirationDate = LocalDateTime.ofEpochSecond(exp, 0, ZoneOffset.UTC);
    }

    @JsonProperty("iat")
    public void setIssuedAt(long iat) {
        issuedAt = LocalDateTime.ofEpochSecond(iat, 0, ZoneOffset.UTC);
    }
}
