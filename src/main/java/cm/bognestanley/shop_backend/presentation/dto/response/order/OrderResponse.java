package cm.bognestanley.shop_backend.presentation.dto.response.order;

import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String status,
        String customerFirstName,
        String customerLastName,
        String customerEmail,
        String customerPhoneNumber,
        String note,
        MoneyResponse totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemResponse> items) {

}
