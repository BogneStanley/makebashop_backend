package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cm.bognestanley.shop_backend.domain.producthighlight.entity.ProductHighlightSlot;
import cm.bognestanley.shop_backend.domain.producthighlight.repository.ProductHighlightRepository;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.producthighlight.HighlightListTypeJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.producthighlight.ProductHighlightSlotJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.ProductHighlightMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductHighlightSlotJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductJpaRepository;

@Repository
public class ProductHighlightRepositoryJpaAdapter implements ProductHighlightRepository {

    private final ProductHighlightSlotJpaRepository slotJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductHighlightMapper productHighlightMapper;

    public ProductHighlightRepositoryJpaAdapter(
            ProductHighlightSlotJpaRepository slotJpaRepository,
            ProductJpaRepository productJpaRepository,
            ProductHighlightMapper productHighlightMapper) {
        this.slotJpaRepository = slotJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.productHighlightMapper = productHighlightMapper;
    }

    @Override
    public List<ProductHighlightSlot> findByListTypeOrderByPosition(HighlightListType listType) {
        HighlightListTypeJpa jpaType = productHighlightMapper.toJpaType(listType);
        return slotJpaRepository.findByListTypeOrderByPositionAsc(jpaType).stream()
                .map(productHighlightMapper::toDomain)
                .toList();
    }

    @Override
    public Map<HighlightListType, List<ProductHighlightSlot>> findAllGroupedByType() {
        Map<HighlightListType, List<ProductHighlightSlot>> grouped = new EnumMap<>(HighlightListType.class);
        for (HighlightListType type : HighlightListType.values()) {
            grouped.put(type, findByListTypeOrderByPosition(type));
        }
        return grouped;
    }

    @Override
    public boolean isConfigured(HighlightListType listType) {
        return slotJpaRepository.existsByListType(productHighlightMapper.toJpaType(listType));
    }

    @Override
    @Transactional
    public void replaceByListType(HighlightListType listType, List<ProductHighlightSlot> slots) {
        HighlightListTypeJpa jpaType = productHighlightMapper.toJpaType(listType);
        slotJpaRepository.deleteByListType(jpaType);

        List<ProductHighlightSlotJpaEntity> entities = slots.stream()
                .map(slot -> {
                    ProductJpaEntity product = productJpaRepository.getReferenceById(slot.getProductId());
                    return productHighlightMapper.toJpa(slot, product);
                })
                .toList();

        slotJpaRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteByListType(HighlightListType listType) {
        slotJpaRepository.deleteByListType(productHighlightMapper.toJpaType(listType));
    }
}
