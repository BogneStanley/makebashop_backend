package cm.bognestanley.shop_backend.presentation.facade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderStatusJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.OrderJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductJpaRepository;
import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.dashboard.DashboardResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DashboardFacade {

    private final OrderJpaRepository orderRepository;
    private final ProductJpaRepository productRepository;
    private final OrderFacade orderFacade;

    @Value("${app.currency-code:FCFA}")
    private String currencyCode;

    public DashboardResponse getDashboard() {
        BigDecimal paidRevenue = orderRepository.sumTotalByOrderStatus(OrderStatusJpa.PAID);

        return new DashboardResponse(
                orderRepository.count(),
                productRepository.countByIsActiveTrue(),
                new MoneyResponse(paidRevenue == null ? BigDecimal.ZERO : paidRevenue, currencyCode),
                orderRepository.countDistinctCustomersByNonStatus(OrderStatusJpa.CANCELLED),
                orderFacade.findAllOrders(0, 5, "createdAt", "desc").content());
    }
}
