package cm.bognestanley.shop_backend.infrastructure.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import cm.bognestanley.shop_backend.infrastructure.config.UploadProperties;
import cm.bognestanley.shop_backend.infrastructure.exception.StorageException;

class ImageUploadValidatorTest {

    private final ImageUploadValidator validator = new ImageUploadValidator(new UploadProperties());

    @Test
    void acceptsARealPngAndUsesDetectedContentType() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "product.png", "image/png", pngBytes());

        var result = validator.validate(file);

        assertEquals("product.png", result.filename());
        assertEquals("image/png", result.contentType());
    }

    @Test
    void rejectsWhenDeclaredTypeDoesNotMatchImageContent() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "product.png", "image/jpeg", pngBytes());

        assertThrows(StorageException.class, () -> validator.validate(file));
    }

    private byte[] pngBytes() throws Exception {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }
}
