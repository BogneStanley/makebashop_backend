package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.entity.CartItem;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.cart.CartJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.cart.CartItemJpaEntity;

@Component
public class CartMapper {

    private final ProductMapper productMapper;

    public CartMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public CartJpaEntity toJpaEntity(Cart domain) {
        if (domain == null) {
            return null;
        }
        CartJpaEntity entity = new CartJpaEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());

        if (domain.getCartItems() != null) {
            entity.setCartItems(domain.getCartItems().stream().map(this::toCartItemJpaEntity)
                    .collect(Collectors.toSet()));
            entity.getCartItems().forEach(item -> item.setCart(entity));
        }

        return entity;
    }

    private CartItemJpaEntity toCartItemJpaEntity(CartItem domain) {
        if (domain == null) {
            return null;
        }
        CartItemJpaEntity entity = new CartItemJpaEntity();
        entity.setId(domain.getId());
        entity.setProduct(productMapper.toJpa(domain.getProduct()));
        entity.setProductVariant(productMapper.toProductVariantJpa(domain.getProductVariant()));
        entity.setQuantity(domain.getQuantity());
        return entity;
    }

    public Cart toDomain(CartJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Cart(
                entity.getId(),
                entity.getUserId(),
                entity.getCartItems().stream().map(this::toCartItemDomain)
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

    private CartItem toCartItemDomain(CartItemJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CartItem(
                entity.getId(),
                productMapper.toDomain(entity.getProduct()),
                productMapper.toProductVariantDomain(entity.getProductVariant()),
                entity.getQuantity());
    }

}
