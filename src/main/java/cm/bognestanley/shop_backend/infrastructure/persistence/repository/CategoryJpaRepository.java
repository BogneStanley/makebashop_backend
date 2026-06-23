package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.category.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
    boolean existsByName(String name);
}
