package org.acme.enumeration;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@AllArgsConstructor
public enum UserMediaFileExtensionEnum {
    JPEG("image/jpeg", ".jpeg"),
    PNG("image/png", ".png");

    public final String mediaType;
    public final String extension;

    public static UserMediaFileExtensionEnum getValueFromMediaType (String mediaType) {
        return Arrays.stream(values())
                .filter(val -> StringUtils.equals(val.mediaType, mediaType))
                .findFirst()
                .orElse(null);
    }
}
