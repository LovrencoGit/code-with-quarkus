package org.acme.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

public class FileUtils {
    public static void writeFile(InputStream is, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static Optional<Path> findFile(Stream<Path> fileList, String fileName) {
        return fileList.filter(e -> e.getFileName().toString().startsWith(fileName))
                .findFirst();
    }
}
