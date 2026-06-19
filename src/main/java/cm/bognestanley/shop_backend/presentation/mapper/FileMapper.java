package cm.bognestanley.shop_backend.presentation.mapper;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;

@Component
public class FileMapper {

    public FileContent toFileContent(MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }
        return new FileContent(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes()
        );
    }

}
