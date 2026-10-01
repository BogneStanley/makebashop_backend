package cm.bognestanley.shop_backend.infrastructure.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String extension = extensionFor(fileContent.contentType());
        String nameToSaveFile = UUID.randomUUID() + "." + extension;

        Path uploadDir = Paths.get(uploadProperties.getDir()).toAbsolutePath().normalize();
        Path targetPath = uploadDir.resolve(nameToSaveFile).normalize();
        ensureWithinUploadDirectory(uploadDir, targetPath);

        try {
            Files.createDirectories(uploadDir);
            Files.write(targetPath, fileContent.content());
        } catch (IOException e) {
            log.error("Failed to upload file to {}: {} ({})", targetPath, e.getMessage(), e.getClass().getSimpleName());
            throw new StorageException("Failed to upload file: " + e.getMessage(), e);
        }

        return new StoredFile(
                nameToSaveFile,
                nameToSaveFile,
                fileContent.contentType(),
                uploadProperties.relativePath(nameToSaveFile));
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            Path uploadDir = Paths.get(uploadProperties.getDir()).toAbsolutePath().normalize();
            Path targetPath = uploadDir.resolve(storageKey).normalize();
            ensureWithinUploadDirectory(uploadDir, targetPath);
            Files.delete(targetPath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            throw new StorageException("Failed to delete file: " + e.getMessage(), e);
        }
    }


    public String getUploadDir() {
        return uploadProperties.getDir();
    }

    private String extensionFor(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            default -> throw new StorageException("Unsupported image content type");
        };
    }

    private void ensureWithinUploadDirectory(Path uploadDir, Path targetPath) {
        if (!targetPath.startsWith(uploadDir)) {
            throw new StorageException("Invalid storage path");
        }
    }

}
