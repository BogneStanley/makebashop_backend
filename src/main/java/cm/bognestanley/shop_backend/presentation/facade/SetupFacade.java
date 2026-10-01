package cm.bognestanley.shop_backend.presentation.facade;

import cm.bognestanley.shop_backend.application.setup.dto.InitialAdminSetupCommand;
import cm.bognestanley.shop_backend.application.setup.usecase.CompleteInitialSetupUsecase;
import cm.bognestanley.shop_backend.presentation.dto.request.setup.InitialAdminSetupRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.setup.SetupStatusResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.user.UserResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SetupFacade {

    private final CompleteInitialSetupUsecase completeInitialSetupUsecase;
    private final PresUserMapper userMapper;

    public SetupFacade(CompleteInitialSetupUsecase completeInitialSetupUsecase, PresUserMapper userMapper) {
        this.completeInitialSetupUsecase = completeInitialSetupUsecase;
        this.userMapper = userMapper;
    }

    public SetupStatusResponse getStatus() {
        ensureAvailable();
        return new SetupStatusResponse(true);
    }

    public UserResponse complete(InitialAdminSetupRequest request) {
        return userMapper.toResponse(completeInitialSetupUsecase.execute(new InitialAdminSetupCommand(
                request.email(), request.firstName(), request.lastName(), request.password())));
    }

    private void ensureAvailable() {
        if (!completeInitialSetupUsecase.isAvailable()) {
            // A completed setup deliberately looks like a missing endpoint.
            throw new cm.bognestanley.shop_backend.application.common.exception.ApplicationException(
                    cm.bognestanley.shop_backend.domain.common.exception.ErrorCode.SETUP_NOT_AVAILABLE);
        }
    }
}
