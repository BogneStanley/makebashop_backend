package cm.bognestanley.shop_backend.presentation.dto.response.cart;

import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;

import java.util.List;

public record CartResponse(
    Long id,
    MoneyResponse totalAmount,
    List<CartItemResponse> items
) {

}
