package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.producthighlight.HighlightListTypeJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.producthighlight.ProductHighlightSlotJpaEntity;

public interface ProductHighlightSlotJpaRepository extends JpaRepository<ProductHighlightSlotJpaEntity, Long> {

    List<ProductHighlightSlotJpaEntity> findByListTypeOrderByPositionAsc(HighlightListTypeJpa listType);

    void deleteByListType(HighlightListTypeJpa listType);

    boolean existsByListType(HighlightListTypeJpa listType);
}
