package cm.bognestanley.shop_backend.presentation.dto.response.producthighlight;

import java.util.List;

import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductResponse;

public record ProductHighlightListConfigResponse(
        boolean configured,
        List<ProductResponse> products) {
}
