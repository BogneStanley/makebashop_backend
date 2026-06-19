package cm.bognestanley.shop_backend.application.common.dto;

public record StoredFile(
    String storageKey,
    String fileName,
    String contentType,
    String fileUrl
) {
}

