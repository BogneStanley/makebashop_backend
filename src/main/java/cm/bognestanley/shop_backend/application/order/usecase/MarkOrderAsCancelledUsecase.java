package cm.bognestanley.shop_backend.application.order.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;

@Service
public class MarkOrderAsCancelledUsecase {

    private final OrderRepository orderRepository;

    public MarkOrderAsCancelledUsecase(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Order execute(Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND));

        order.markAsCancelled();

        return orderRepository.save(order);
    }

}
