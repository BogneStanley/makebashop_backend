package cm.bognestanley.shop_backend.application.user.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.exception.UserNotFoundException;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Service
public class GetOneUserUsecase {

    private final UserRepository userRepository;

    public GetOneUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }
}
