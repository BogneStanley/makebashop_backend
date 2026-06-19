package cm.bognestanley.shop_backend.application.order.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;

@Service
public class SearchOrderUsecase {

    private final OrderRepository orderRepository;

    public SearchOrderUsecase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PaginatedEntity<Order> execute(
            String orderNumber,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PaginationAttribute paginationAttribute) {
        return orderRepository.search(orderNumber, status, startDate, endDate, paginationAttribute);
    }

}
