package cm.bognestanley.shop_backend.application.user.usecase;

import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.application.user.dto.LoginCommand;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.exception.BadUserCredentials;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class LoginUsecase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public LoginUsecase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(LoginCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new BadUserCredentials("Invalid email or password"));

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new BadUserCredentials("Invalid email or password");
        }

        return user;
    }
}
