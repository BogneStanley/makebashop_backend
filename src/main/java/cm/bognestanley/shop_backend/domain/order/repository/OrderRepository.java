package cm.bognestanley.shop_backend.domain.order.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;

public interface OrderRepository {
    Optional<Order> findById(Long id);

    PaginatedEntity<Order> findAll(PaginationAttribute paginationAttribute);

    PaginatedEntity<Order> search(String orderNumber, String status, LocalDateTime startDate, LocalDateTime endDate, PaginationAttribute paginationAttribute);

    Order save(Order order);
}
