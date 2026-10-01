package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.setup.ApplicationSetupJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationSetupJpaRepository extends JpaRepository<ApplicationSetupJpaEntity, Short> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select state from ApplicationSetupJpaEntity state where state.id = 1")
    Optional<ApplicationSetupJpaEntity> findSingletonForUpdate();
}
