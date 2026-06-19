package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.user.UserJpaEntity;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long>, JpaSpecificationExecutor<UserJpaEntity> {
    boolean existsByEmail(String email);
    Optional<UserJpaEntity> findByEmail(String email);
}
