package cm.bognestanley.shop_backend.application.order.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import jakarta.transaction.Transactional;

@Service
public class MarkOrderAsPaidUsecase {

    private final OrderRepository orderRepository;

    public MarkOrderAsPaidUsecase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order execute(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND));

        order.markAsPaid();

        return orderRepository.save(order);
    }

}
