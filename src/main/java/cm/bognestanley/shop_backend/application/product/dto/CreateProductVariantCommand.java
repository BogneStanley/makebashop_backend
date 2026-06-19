package cm.bognestanley.shop_backend.application.product.dto;

import java.math.BigDecimal;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;

public record CreateProductVariantCommand(
    String sku,
    BigDecimal price,
    String currencyCode,
    int stockQuantity,
    String color,
    String size
) {

    public ProductVariant toDomainEntity() {
        return ProductVariant.create(this.sku(), new Money(this.price(), this.currencyCode()), this.stockQuantity(), this.color(), this.size());
    }
}