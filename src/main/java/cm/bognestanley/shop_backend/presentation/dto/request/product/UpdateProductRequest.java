package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.util.List;

public record UpdateProductRequest(
    String name,
    String description,
    List<Long> categoryIds
) {
}
