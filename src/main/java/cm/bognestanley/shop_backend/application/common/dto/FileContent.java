package cm.bognestanley.shop_backend.application.common.dto;

public record FileContent(
    String filename,
    String contentType,
    byte[] content
) {
}
