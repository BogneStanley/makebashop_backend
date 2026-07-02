package cm.bognestanley.shop_backend.presentation.dto.response.producthighlight;

public record ProductHighlightConfigResponse(
        ProductHighlightListConfigResponse newProducts,
        ProductHighlightListConfigResponse popularProducts,
        ProductHighlightListConfigResponse featuredProducts) {
}
