package cm.bognestanley.shop_backend.infrastructure.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.infrastructure.config.UploadProperties;
import cm.bognestanley.shop_backend.infrastructure.exception.StorageException;

@Component
public class ImageUploadValidator {

    private static final Map<String, String> CONTENT_TYPES_BY_FORMAT = Map.of(
            "JPEG", "image/jpeg",
            "PNG", "image/png");

    private final UploadProperties uploadProperties;

    public ImageUploadValidator(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    public FileContent validate(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new StorageException("An image file is required");
        }
        if (file.getSize() > uploadProperties.getMaxFileSizeBytes()) {
            throw new StorageException("Image exceeds the allowed size");
        }

        byte[] content = file.getBytes();
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(content))) {
            if (input == null) {
                throw new StorageException("Invalid image content");
            }
            var readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new StorageException("Unsupported image format");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(input, true, true);
                String format = reader.getFormatName().toUpperCase(Locale.ROOT);
                String contentType = CONTENT_TYPES_BY_FORMAT.get(format);
                if (contentType == null) {
                    throw new StorageException("Only JPEG and PNG images are allowed");
                }

                long pixels = (long) reader.getWidth(0) * reader.getHeight(0);
                if (pixels <= 0 || pixels > uploadProperties.getMaxImagePixels()) {
                    throw new StorageException("Image dimensions exceed the allowed limit");
                }

                String declaredContentType = file.getContentType();
                if (declaredContentType != null && !declaredContentType.equalsIgnoreCase(contentType)) {
                    throw new StorageException("Image content type does not match its content");
                }

                return new FileContent(file.getOriginalFilename(), contentType, content);
            } finally {
                reader.dispose();
            }
        }
    }
}
