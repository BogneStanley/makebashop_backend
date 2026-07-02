package cm.bognestanley.shop_backend.application.producthighlight.dto;

import java.util.List;
import java.util.Map;

import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;

public record ProductHighlightConfigResult(
        Map<HighlightListType, ProductHighlightListConfig> lists) {

    public record ProductHighlightListConfig(
            boolean configured,
            List<Product> products) {
    }
}
