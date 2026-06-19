package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.cart.CartJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.CartMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.CartJpaRepository;

@Repository
public class CartRepositoryJpaAdapter implements CartRepository {

    private final CartJpaRepository cartJpaRepository;
    private final CartMapper cartMapper;

    public CartRepositoryJpaAdapter(CartJpaRepository cartJpaRepository, CartMapper cartMapper) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Cart save(Cart cart) {
        CartJpaEntity entity = cartMapper.toJpaEntity(cart);
        CartJpaEntity savedCartEntity = cartJpaRepository.save(entity);
        Cart savedCart = cartMapper.toDomain(savedCartEntity);
        return savedCart;
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartJpaRepository.findByUserId(userId).map(cartMapper::toDomain);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartJpaRepository.findById(id).map(cartMapper::toDomain);
    }

    @Override
    public void delete(Cart cart) {
        cartJpaRepository.delete(cartMapper.toJpaEntity(cart));
    }

}
