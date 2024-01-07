package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.FailureException;
import org.acme.configuration.UserMediaConfiguration;
import org.acme.enumeration.ResponseEnum;
import org.acme.enumeration.UserMediaFileExtensionEnum;
import org.acme.util.FileUtils;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class UserMediaService {
    final UserMediaConfiguration userMediaConfiguration;

    public void uploadProfileImage(String userId, MultipartFormDataInput inputFile) {
        inputFile.getValues().get("inputFile")
                .forEach(file -> {
                    try (InputStream is = file.getFileItem().getInputStream()) {
                        String filePath = generateFolderPath(userId) + generateFileNameWithExtension(userId, defineFileExtension(file));

                        FileUtils.writeFile(is, filePath);
                    } catch (IOException ex) {
                        log.error("Error while writing file with name {}\n {}", file.getFileName(), ex.getStackTrace());
                        throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                                "INTERNAL_SERVER_ERROR - Error while writing file");
                    }
                });
    }

    public Path downloadProfileImage(String userId) {
        String folderPath = generateFolderPath(userId);
        String fileName = generateFileName(userId);

        Path folder = Paths.get(folderPath);
        try (Stream<Path> fileList = Files.list(folder)) {
            return FileUtils.findFile(fileList, fileName)
                    .orElseThrow(
                            () -> new FailureException(Response.Status.NOT_FOUND, ResponseEnum.NOT_FOUND,
                                    "NOT_FOUND - No file found with name: '" + fileName + "'"));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                    "INTERNAL_SERVER_ERROR - Error occured while retrieving a file");
        }
    }

    private String generateFolderPath(String userId) {
        return userMediaConfiguration.folder().path().replace(userMediaConfiguration.placeholder(), userId + File.separator);
    }

    private String generateFileNameWithExtension(String userId, String extension) {
        return generateFileName(userId).concat(extension);
    }

    private String generateFileName(String userId) {
        return userMediaConfiguration.placeholder().concat(userMediaConfiguration.filenameSuffix())
                .replace(userMediaConfiguration.placeholder(), userId);
    }

    private String defineFileExtension(FormValue file) {
        UserMediaFileExtensionEnum extEnum = UserMediaFileExtensionEnum
                .getValueFromMediaType(file.getHeaders().get("Content-Type").get(0));

        return extEnum != null ? extEnum.extension : "";
    }
}
