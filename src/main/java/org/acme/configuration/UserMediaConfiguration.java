package org.acme.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "filesystem.media.images.user.profile")
public interface UserMediaConfiguration {
    @WithName("filename.suffix")
    String filenameSuffix();

    String placeholder();

    Folder folder();

    interface Folder {
        String path();
    }
}
