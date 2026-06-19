package cm.bognestanley.shop_backend.infrastructure.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long>, JpaSpecificationExecutor<ProductJpaEntity> {
}
