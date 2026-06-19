package cm.bognestanley.shop_backend.presentation.dto.response.cart;


import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductVariantResponse;

public record CartItemResponse(
        Long id,
        ProductResponse product,
        ProductVariantResponse variant,
        int quantity,
        MoneyResponse subtotal) {

}
