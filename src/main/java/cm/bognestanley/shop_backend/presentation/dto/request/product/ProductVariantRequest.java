package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductVariantRequest(
        @NotBlank(message = "Variant SKU is required") String sku,
        @NotNull(message = "Price is required") BigDecimal price,
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be non-negative")
        Integer stockQuantity,
        @NotBlank(message = "Size is required") String size,
        @NotBlank(message = "Color is required") String color) {

}
