package cm.bognestanley.shop_backend.application.user.usecase;

import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.application.user.dto.RegisterUserCommand;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.exception.EmailAlreadyExistsException;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class RegisterUserUsecase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserUsecase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(RegisterUserCommand command) {
        String normalizedEmail = command.email().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.createCustomer(
                normalizedEmail,
                command.firstName(),
                command.lastName(),
                command.avatar(),
                passwordEncoder.encode(command.password())
        );

        return userRepository.save(user);
    }
}
