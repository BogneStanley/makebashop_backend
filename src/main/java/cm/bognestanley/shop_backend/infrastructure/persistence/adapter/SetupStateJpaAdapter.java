package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import cm.bognestanley.shop_backend.application.setup.port.SetupStatePort;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.setup.ApplicationSetupJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ApplicationSetupJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SetupStateJpaAdapter implements SetupStatePort {

    private static final short SINGLETON_ID = 1;

    private final ApplicationSetupJpaRepository repository;

    public SetupStateJpaAdapter(ApplicationSetupJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isSetupCompleted() {
        return state().getCompletedAt() != null;
    }

    @Override
    public boolean lockAndIsSetupCompleted() {
        return lockedState().getCompletedAt() != null;
    }

    @Override
    public void markSetupCompleted() {
        ApplicationSetupJpaEntity state = lockedState();
        state.markCompleted();
    }

    private ApplicationSetupJpaEntity state() {
        return repository.findById(SINGLETON_ID)
                .orElseThrow(() -> new IllegalStateException("Application setup state is missing"));
    }

    private ApplicationSetupJpaEntity lockedState() {
        return repository.findSingletonForUpdate()
                .orElseThrow(() -> new IllegalStateException("Application setup state is missing"));
    }
}
