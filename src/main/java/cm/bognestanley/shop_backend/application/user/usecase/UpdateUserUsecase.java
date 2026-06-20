package cm.bognestanley.shop_backend.application.user.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.application.user.dto.UpdateUserCommand;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Service
public class UpdateUserUsecase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UpdateUserUsecase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(UpdateUserCommand command) {
        if (command == null || command.id() == null) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "User ID cannot be null");
        }

        User existingUser = userRepository.findById(command.id())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND, "User with ID " + command.id() + " not found"));

        String normalizedEmail = normalizeEmail(command.email());
        if (normalizedEmail != null
                && !normalizedEmail.equalsIgnoreCase(existingUser.getEmail())
                && userRepository.existsByEmail(normalizedEmail)) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST, "Email already exists");
        }

        existingUser.updateProfile(normalizedEmail, command.firstName(), command.lastName(), command.avatar(), command.role());

        if (command.password() != null && !command.password().isBlank()) {
            existingUser.changePassword(passwordEncoder.encode(command.password()));
        }

        return userRepository.save(existingUser);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim();
    }
}
