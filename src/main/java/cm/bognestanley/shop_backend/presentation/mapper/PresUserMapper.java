package cm.bognestanley.shop_backend.presentation.mapper;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.user.dto.RegisterUserCommand;
import cm.bognestanley.shop_backend.application.user.dto.UpdateUserCommand;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.presentation.dto.request.user.CreateUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.user.UpdateUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.user.UserResponse;

@Component
public class PresUserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getRole(),
                user.isActivate(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public RegisterUserCommand toCreateUserCommand(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        return new RegisterUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.avatar(),
                request.role());
    }

    public UpdateUserCommand toUpdateUserCommand(Long userId, UpdateUserRequest request) {
        if (request == null) {
            return new UpdateUserCommand(userId, null, null, null, null, null, null);
        }

        return new UpdateUserCommand(
                userId,
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.avatar(),
                request.role());
    }
}
