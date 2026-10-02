package cm.bognestanley.shop_backend.presentation.dto.response.dashboard;

import java.util.List;

import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.order.OrderResponse;

public record DashboardResponse(
        long orderCount,
        long activeProductCount,
        MoneyResponse revenue,
        long customerCount,
        List<OrderResponse> recentOrders) {
}
