package cm.bognestanley.shop_backend.presentation.dto.request.cart;

import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
        @NotNull(message = "Product id is required")
        Long productId,
        @NotNull(message = "Product variant id is required")
        Long variantId,
        @NotNull(message = "Quantity is required")
        int quantity
) {

}
