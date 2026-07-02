package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.domain.producthighlight.entity.ProductHighlightSlot;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.producthighlight.HighlightListTypeJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.producthighlight.ProductHighlightSlotJpaEntity;

@Component
public class ProductHighlightMapper {

    public ProductHighlightSlot toDomain(ProductHighlightSlotJpaEntity entity) {
        return new ProductHighlightSlot(
                entity.getId(),
                toDomainType(entity.getListType()),
                entity.getProduct().getId(),
                entity.getPosition());
    }

    public ProductHighlightSlotJpaEntity toJpa(ProductHighlightSlot slot, ProductJpaEntity product) {
        return ProductHighlightSlotJpaEntity.builder()
                .id(slot.getId())
                .listType(toJpaType(slot.getListType()))
                .product(product)
                .position(slot.getPosition())
                .build();
    }

    public HighlightListType toDomainType(HighlightListTypeJpa type) {
        return HighlightListType.valueOf(type.name());
    }

    public HighlightListTypeJpa toJpaType(HighlightListType type) {
        return HighlightListTypeJpa.valueOf(type.name());
    }
}
