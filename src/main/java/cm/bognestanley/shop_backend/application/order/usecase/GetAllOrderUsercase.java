package cm.bognestanley.shop_backend.application.order.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;

@Service
public class GetAllOrderUsercase {

    private final OrderRepository orderRepository;

    public GetAllOrderUsercase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PaginatedEntity<Order> execute(PaginationAttribute paginationAttribute) {
        return orderRepository.findAll(paginationAttribute);
    }
    
}
