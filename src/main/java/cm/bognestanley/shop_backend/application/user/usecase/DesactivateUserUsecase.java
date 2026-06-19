package cm.bognestanley.shop_backend.application.user.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.exception.UserNotFoundException;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Service
public class DesactivateUserUsecase {

    private final UserRepository userRepository;

    public DesactivateUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        user.desactivate();
        return userRepository.save(user);
    }
}
