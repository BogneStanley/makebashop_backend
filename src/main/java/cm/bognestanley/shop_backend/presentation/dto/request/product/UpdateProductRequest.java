package cm.bognestanley.shop_backend.presentation.dto.request.product;

public record UpdateProductRequest(
    String name,
    String description
) {
}
