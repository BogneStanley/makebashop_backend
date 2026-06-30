package cm.bognestanley.shop_backend.infrastructure.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.application.common.dto.StoredFile;
import cm.bognestanley.shop_backend.application.common.port.FileStoragePort;
import cm.bognestanley.shop_backend.infrastructure.config.UploadProperties;
import cm.bognestanley.shop_backend.infrastructure.exception.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalStorageAdapter implements FileStoragePort{

    private final UploadProperties uploadProperties;

    @Override
    public StoredFile uploadFile(FileContent fileContent) {
        Optional<String> extension = getFileExtension(fileContent.filename());

        if(extension.isEmpty()) {
            throw new StorageException("File extension is required");
        }

        String nameToSaveFile = getFilenameWithoutExtension(fileContent.filename()) + "-" + UUID.randomUUID() + "." + extension.get();

        Path targetPath = Paths.get(uploadProperties.getDir(), nameToSaveFile);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, fileContent.content());
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new StorageException("Failed to upload file: " + e.getMessage(), e);
        }

        return new StoredFile(
                nameToSaveFile,
                fileContent.filename(),
                fileContent.contentType(),
                uploadProperties.relativePath(nameToSaveFile));
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            Path targetPath = Paths.get(uploadProperties.getDir(), storageKey);
            Files.delete(targetPath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            throw new StorageException("Failed to delete file: " + e.getMessage(), e);
        }
    }


    public String getUploadDir() {
        return uploadProperties.getDir();
    }

    private Optional<String> getFileExtension(String filename){
        List<String> parts = Arrays.stream(filename.split("\\.")).toList();
        if (parts.size() < 2) {
            return Optional.empty();
        }
        return Optional.of(parts.getLast());
    }

    private String getFilenameWithoutExtension(String filename){
        return Arrays.stream(filename.split("\\.")).toList().getFirst().replace(" ","");
    }

}
