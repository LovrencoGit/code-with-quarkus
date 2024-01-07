package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.FailureException;
import org.acme.enumeration.ResponseEnum;
import org.acme.response.FileListResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ApplicationScoped
public class MediaService {
    @ConfigProperty(name = "test.media.resources.directory.path")
    private String directoryPath;

    public void uploadFiles(MultipartFormDataInput input) {
        Map<String, Collection<FormValue>> inputValues = input.getValues();
        inputValues.get("input").forEach(e -> {
            try (InputStream is = e.getFileItem().getInputStream()) {
                Path path = Paths.get(directoryPath + e.getFileName());
                File dir = new File(directoryPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                log.error("Error while writing file with name {}\n {}", e.getFileName(), ex.getStackTrace());
            }
        });
    }

    public Path downloadFile(String fileName) {
        return findFile(fileName);
    }

    public void deleteFile(String fileName) {
        try {
            Files.deleteIfExists(findFile(fileName));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                    "INTERNAL_SERVER_ERROR - Error occured while deleting a file");
        }
    }

    private Path findFile(String fileName) {
        Path dir = Paths.get(directoryPath);

        try (Stream<Path> fileList = Files.list(dir)) {
            return fileList.filter(e -> e.getFileName().toString().startsWith(fileName))
                    .findFirst()
                    .orElseThrow(
                            () -> new FailureException(Response.Status.NOT_FOUND, ResponseEnum.NOT_FOUND,
                                    "NOT_FOUND - No file found with name: '" + fileName + "'"));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                    "INTERNAL_SERVER_ERROR - Error occured while retrieving a file");
        }
    }

    public FileListResponse listFiles() {
        Path dir = Paths.get(directoryPath);

        try (Stream<Path> fileList = Files.list(dir)) {
            List<String> dirFiles = fileList
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());

            return FileListResponse.builder()
                    .files(dirFiles)
                    .size(dirFiles.size())
                    .build();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR,
                    "INTERNAL_SERVER_ERROR - Error occured while retrieving the file list");
        }
    }
}
