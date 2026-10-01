package cm.bognestanley.shop_backend.infrastructure.inventory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.order.port.InventoryReservationPort;
import cm.bognestanley.shop_backend.domain.cart.entity.CartItem;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.order.entity.OrderLineItem;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.ProductMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductVariantJpaRepository;

@Component
public class InventoryReservationJpaAdapter implements InventoryReservationPort {

    private final ProductVariantJpaRepository productVariantRepository;
    private final ProductMapper productMapper;

    public InventoryReservationJpaAdapter(ProductVariantJpaRepository productVariantRepository, ProductMapper productMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<OrderLineItem> reserve(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new DomainErrorException(ErrorCode.CART_EMPTY);
        }

        Map<Long, Integer> quantities = quantitiesByVariantId(cartItems);
        Map<Long, ProductVariantJpaEntity> variants = lockedVariants(quantities.keySet());

        for (CartItem item : cartItems) {
            ProductVariantJpaEntity variant = variants.get(item.getProductVariant().getId());
            if (variant == null || item.getProduct() == null || item.getProduct().getId() == null
                    || !item.getProduct().getId().equals(variant.getProduct().getId())) {
                throw new DomainErrorException(ErrorCode.INVALID_CART_ITEM);
            }
            if (!variant.getProduct().isActive()) {
                throw new DomainErrorException(ErrorCode.PRODUCT_INACTIVE);
            }
        }

        quantities.forEach((variantId, quantity) -> decrease(variants.get(variantId), quantity));
        productVariantRepository.saveAll(variants.values());

        return cartItems.stream()
                .map(item -> OrderLineItem.create(
                        item.getProduct(),
                        productMapper.toProductVariantDomain(variants.get(item.getProductVariant().getId())),
                        item.getQuantity()))
                .toList();
    }

    @Override
    public void release(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            return;
        }
        Map<Long, Integer> quantities = quantitiesByOrderVariantId(orderLineItems);
        Map<Long, ProductVariantJpaEntity> variants = lockedVariants(quantities.keySet());
        quantities.forEach((variantId, quantity) -> {
            ProductVariantJpaEntity variant = variants.get(variantId);
            variant.setStockQuantity(variant.getStockQuantity() + quantity);
            variant.setUpdatedAt(LocalDateTime.now());
        });
        productVariantRepository.saveAll(variants.values());
    }

    private Map<Long, ProductVariantJpaEntity> lockedVariants(Collection<Long> variantIds) {
        Map<Long, ProductVariantJpaEntity> variants = productVariantRepository.findAllByIdInForUpdate(variantIds)
                .stream().collect(Collectors.toMap(ProductVariantJpaEntity::getId, Function.identity()));
        if (variants.size() != variantIds.size()) {
            throw new DomainErrorException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }
        return variants;
    }

    private Map<Long, Integer> quantitiesByVariantId(List<CartItem> cartItems) {
        Map<Long, Integer> quantities = new HashMap<>();
        for (CartItem item : cartItems) {
            if (item.getProductVariant() == null || item.getProductVariant().getId() == null || item.getQuantity() <= 0) {
                throw new DomainErrorException(ErrorCode.INVALID_CART_ITEM);
            }
            quantities.merge(item.getProductVariant().getId(), item.getQuantity(), Integer::sum);
        }
        return quantities;
    }

    private Map<Long, Integer> quantitiesByOrderVariantId(List<OrderLineItem> orderLineItems) {
        Map<Long, Integer> quantities = new HashMap<>();
        for (OrderLineItem item : orderLineItems) {
            quantities.merge(item.getProductVariant().getId(), item.getQuantity(), Integer::sum);
        }
        return quantities;
    }

    private void decrease(ProductVariantJpaEntity variant, int quantity) {
        if (variant.getStockQuantity() < quantity) {
            throw new DomainErrorException(ErrorCode.OUT_OF_STOCK);
        }
        variant.setStockQuantity(variant.getStockQuantity() - quantity);
        variant.setUpdatedAt(LocalDateTime.now());
    }
}
