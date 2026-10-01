package cm.bognestanley.shop_backend.application.setup.usecase;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.application.setup.dto.InitialAdminSetupCommand;
import cm.bognestanley.shop_backend.application.setup.port.SetupStatePort;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.entity.UserRole;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompleteInitialSetupUsecase {

    private final UserRepository userRepository;
    private final SetupStatePort setupStatePort;
    private final PasswordEncoderPort passwordEncoder;

    public CompleteInitialSetupUsecase(
            UserRepository userRepository,
            SetupStatePort setupStatePort,
            PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.setupStatePort = setupStatePort;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isAvailable() {
        return !setupStatePort.isSetupCompleted() && !userRepository.existsByRole(UserRole.ADMIN);
    }

    @Transactional
    public User execute(InitialAdminSetupCommand command) {
        if (setupStatePort.lockAndIsSetupCompleted() || userRepository.existsByRole(UserRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.SETUP_NOT_AVAILABLE);
        }

        String email = command.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST, "Email already exists");
        }
        validatePassword(command.password());

        User admin = User.createAdmin(
                email,
                command.firstName().trim(),
                command.lastName().trim(),
                null,
                passwordEncoder.encode(command.password()));
        User savedAdmin = userRepository.save(admin);
        setupStatePort.markSetupCompleted();
        return savedAdmin;
    }

    private void validatePassword(String password) {
        boolean isStrong = password.length() >= 14
                && password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isLowerCase)
                && password.chars().anyMatch(Character::isDigit)
                && password.chars().anyMatch(character -> !Character.isLetterOrDigit(character));
        if (!isStrong) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT,
                    "Password must be at least 14 characters and contain uppercase, lowercase, digit and symbol");
        }
    }
}
