package cm.bognestanley.shop_backend.application.user.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Service
public class DeleteUserUsecase {

    private final UserRepository userRepository;

    public DeleteUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND, "User with ID " + userId + " not found"));

        user.delete();
        userRepository.delete(user);
    }
}
