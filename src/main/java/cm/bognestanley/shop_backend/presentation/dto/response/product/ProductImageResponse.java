package cm.bognestanley.shop_backend.presentation.dto.response.product;

public record ProductImageResponse(
    Long id,
    String url,
    String name,
    Integer position,
    Boolean isPrimary
) {
}
