package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.cart.CartJpaEntity;

public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {
    Optional<CartJpaEntity> findByUserId(Long userId);
}
