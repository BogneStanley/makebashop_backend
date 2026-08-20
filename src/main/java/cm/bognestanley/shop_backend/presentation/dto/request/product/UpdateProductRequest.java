package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.util.List;

public record UpdateProductRequest(
    String name,
    String description,
    String details,
    String shippingInfo,
    Boolean isActive,
    List<Long> categoryIds
) {
}
