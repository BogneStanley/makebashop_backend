package cm.bognestanley.shop_backend.application.user.usecase;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.application.user.dto.RegisterUserCommand;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class CreateUserUsecase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUserUsecase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(RegisterUserCommand command) {
        String normalizedEmail = command.email();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST, "Email already exists");
        }

        User user = User.create(
                normalizedEmail,
                command.firstName(),
                command.lastName(),
                command.avatar(),
                passwordEncoder.encode(command.password()),
                command.role()
        );

        return userRepository.save(user);
    }
}
