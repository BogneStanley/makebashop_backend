package cm.bognestanley.shop_backend.application.cart.port;

import cm.bognestanley.shop_backend.domain.cart.entity.Cart;

public interface CartResolver {
    Cart resolveCart();
    Cart resolveCart(Long cartId);
}
