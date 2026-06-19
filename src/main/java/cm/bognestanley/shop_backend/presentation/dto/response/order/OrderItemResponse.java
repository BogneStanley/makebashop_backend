package cm.bognestanley.shop_backend.presentation.dto.response.order;


import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductVariantResponse;

public record OrderItemResponse(
                Long id,
                ProductResponse product,
                ProductVariantResponse variant,
                int quantity,
                MoneyResponse price,
                MoneyResponse subtotal) {

}
