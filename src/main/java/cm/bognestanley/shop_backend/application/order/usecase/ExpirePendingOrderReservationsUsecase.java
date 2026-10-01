package cm.bognestanley.shop_backend.application.order.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.bognestanley.shop_backend.application.order.port.InventoryReservationPort;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;

@Service
public class ExpirePendingOrderReservationsUsecase {

    private final OrderRepository orderRepository;
    private final InventoryReservationPort inventoryReservation;

    public ExpirePendingOrderReservationsUsecase(
            OrderRepository orderRepository, InventoryReservationPort inventoryReservation) {
        this.orderRepository = orderRepository;
        this.inventoryReservation = inventoryReservation;
    }

    @Transactional
    public int execute(LocalDateTime now) {
        var orders = orderRepository.findExpiredPendingReservations(now);
        for (var order : orders) {
            order.markAsCancelled();
            inventoryReservation.release(order.getOrderLineItems());
            orderRepository.save(order);
        }
        return orders.size();
    }
}
