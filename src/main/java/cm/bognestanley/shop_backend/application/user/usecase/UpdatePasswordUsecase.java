package cm.bognestanley.shop_backend.application.user.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.application.user.dto.UpdatePasswordCommand;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;

@Service
public class UpdatePasswordUsecase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UpdatePasswordUsecase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(UpdatePasswordCommand command) {
        if (command == null || command.userId() == null) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "User ID cannot be null");
        }

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND,
                        "User with ID " + command.userId() + " not found"));

        if (!passwordEncoder.matches(command.currentPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "Current password is incorrect");
        }

        user.changePassword(passwordEncoder.encode(command.newPassword()));
        userRepository.save(user);
    }
}
