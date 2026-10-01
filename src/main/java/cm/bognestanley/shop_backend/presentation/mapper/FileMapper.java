package cm.bognestanley.shop_backend.presentation.mapper;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.infrastructure.storage.ImageUploadValidator;

@Component
public class FileMapper {

    private final ImageUploadValidator imageUploadValidator;

    public FileMapper(ImageUploadValidator imageUploadValidator) {
        this.imageUploadValidator = imageUploadValidator;
    }

    public FileContent toFileContent(MultipartFile file) throws IOException {
        return imageUploadValidator.validate(file);
    }

}
