package cm.bognestanley.shop_backend.presentation.dto.response.product;

import java.io.Serializable;

import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;

public record ProductVariantResponse(
    Long id,
    String sku,
    MoneyResponse price,
    int stockQuantity,
    String color,
    String size
) implements Serializable {
}
