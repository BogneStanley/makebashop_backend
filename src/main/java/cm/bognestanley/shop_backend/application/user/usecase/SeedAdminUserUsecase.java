package cm.bognestanley.shop_backend.application.user.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.entity.UserRole;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Service
public class SeedAdminUserUsecase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public SeedAdminUserUsecase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean execute(String email, String firstName, String lastName, String password) {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return false;
        }

        String normalizedEmail = email.trim().toLowerCase();
        User admin = User.createAdmin(
                normalizedEmail,
                firstName,
                lastName,
                null,
                passwordEncoder.encode(password));

        userRepository.save(admin);
        return true;
    }
}
