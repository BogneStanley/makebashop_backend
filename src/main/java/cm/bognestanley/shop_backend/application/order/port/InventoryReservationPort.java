package cm.bognestanley.shop_backend.application.order.port;

import java.util.List;

import cm.bognestanley.shop_backend.domain.cart.entity.CartItem;
import cm.bognestanley.shop_backend.domain.order.entity.OrderLineItem;

/**
 * Atomically reserves/release stock for a checkout. Implementations must lock
 * the persisted variants; cart snapshots must never be used as stock authority.
 */
public interface InventoryReservationPort {
    List<OrderLineItem> reserve(List<CartItem> cartItems);

    void release(List<OrderLineItem> orderLineItems);
}
