package cm.bognestanley.shop_backend.application.order.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.order.port.InventoryReservationPort;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;

@Service
public class MarkOrderAsCancelledUsecase {

    private final OrderRepository orderRepository;
    private final InventoryReservationPort inventoryReservation;

    public MarkOrderAsCancelledUsecase(OrderRepository orderRepository, InventoryReservationPort inventoryReservation){
        this.orderRepository = orderRepository;
        this.inventoryReservation = inventoryReservation;
    }

    @Transactional
    public Order execute(Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND));

        order.markAsCancelled();
        inventoryReservation.release(order.getOrderLineItems());

        return orderRepository.save(order);
    }

}
