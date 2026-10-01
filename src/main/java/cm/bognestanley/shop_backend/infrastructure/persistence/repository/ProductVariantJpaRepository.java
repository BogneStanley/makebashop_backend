package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import jakarta.persistence.LockModeType;

public interface ProductVariantJpaRepository extends JpaRepository<ProductVariantJpaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select variant from ProductVariantJpaEntity variant
            join fetch variant.product
            where variant.id in :variantIds
            """)
    List<ProductVariantJpaEntity> findAllByIdInForUpdate(@Param("variantIds") Collection<Long> variantIds);
}
