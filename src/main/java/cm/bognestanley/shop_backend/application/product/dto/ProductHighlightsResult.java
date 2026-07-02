package cm.bognestanley.shop_backend.application.product.dto;

import java.util.List;

import cm.bognestanley.shop_backend.domain.product.entity.Product;

public record ProductHighlightsResult(
        List<Product> newProducts,
        List<Product> popularProducts,
        List<Product> featuredProducts) {
}
