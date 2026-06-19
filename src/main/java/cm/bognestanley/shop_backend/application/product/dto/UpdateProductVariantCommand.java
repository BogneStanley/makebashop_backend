package cm.bognestanley.shop_backend.application.product.dto;

import java.math.BigDecimal;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;

public record UpdateProductVariantCommand(
    Long id,
    String sku,
    BigDecimal price,
    String currencyCode,
    int stockQuantity,
    String size,
    String color
) {

    public ProductVariant toDomainEntity(){
        return new ProductVariant(
            this.id,
            this.sku,
            new Money(this.price, this.currencyCode),
            this.stockQuantity,
            this.size,
            this.color,
            null,
            null
        );
    }
}
