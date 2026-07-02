package cm.bognestanley.shop_backend.presentation.dto.response.product;

import java.util.List;

public record ProductHighlightsResponse(
        List<ProductResponse> newProducts,
        List<ProductResponse> popularProducts,
        List<ProductResponse> featuredProducts) {
}
