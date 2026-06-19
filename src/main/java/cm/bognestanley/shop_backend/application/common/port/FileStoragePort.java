package cm.bognestanley.shop_backend.application.common.port;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.application.common.dto.StoredFile;

public interface FileStoragePort {
    StoredFile uploadFile(FileContent fileContent);
    void deleteFile(String storageKey);
}
