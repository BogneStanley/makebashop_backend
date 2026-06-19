package cm.bognestanley.shop_backend.domain.cart.repository;

import java.util.Optional;

import cm.bognestanley.shop_backend.domain.cart.entity.Cart;

public interface CartRepository {
    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findById(Long id);

    Cart save(Cart cart);

    void delete(Cart cart);
}
