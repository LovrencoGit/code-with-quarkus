package org.acme.entity.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RolesEnum {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    CONSULTANT("CONSULTANT");

    private final String value;
}
