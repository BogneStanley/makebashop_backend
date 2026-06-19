package cm.bognestanley.shop_backend.presentation.facade;

import cm.bognestanley.shop_backend.application.user.dto.RegisterUserCommand;
import cm.bognestanley.shop_backend.application.user.usecase.ActivateUserUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.CreateUserUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.DeleteUserUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.DesactivateUserUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.GetAllUsersUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.GetOneUserUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.UpdateUserUsecase;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.infrastructure.security.CurrentUserProvider;
import cm.bognestanley.shop_backend.presentation.dto.request.user.CreateUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.user.UpdateUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.user.UserResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final CreateUserUsecase createUserUsecase;
    private final GetAllUsersUsecase getAllUsersUsecase;
    private final GetOneUserUsecase getOneUserUsecase;
    private final UpdateUserUsecase updateUserUsecase;
    private final DeleteUserUsecase deleteUserUsecase;
    private final ActivateUserUsecase activateUserUsecase;
    private final DesactivateUserUsecase desactivateUserUsecase;
    private final CurrentUserProvider currentUserProvider;
    private final PresUserMapper userMapper;

    public UserResponse createUser(CreateUserRequest request) {
        requireAdmin();

        RegisterUserCommand command = userMapper.toCreateUserCommand(request);

        User user = createUserUsecase.execute(command);
        return userMapper.toResponse(user);
    }

    public PaginatedEntity<UserResponse> getAllUsers(PaginationAttribute attribute) {
        requireAdmin();

        PaginatedEntity<User> users = getAllUsersUsecase.execute(attribute);
        return users.map(userMapper::toResponse);
    }

    public UserResponse getProfile() {
        Long currentUserId = getCurrentUserId();
        User user = getOneUserUsecase.execute(currentUserId);
        return userMapper.toResponse(user);
    }

    public UserResponse updateProfile(UpdateUserRequest request) {
        Long currentUserId = getCurrentUserId();
        UpdateUserRequest profileRequest = new UpdateUserRequest(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.avatar(),
                null);

        User user = updateUserUsecase.execute(userMapper.toUpdateUserCommand(currentUserId, profileRequest));
        return userMapper.toResponse(user);
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        requireAdmin();

        User user = updateUserUsecase.execute(userMapper.toUpdateUserCommand(userId, request));
        return userMapper.toResponse(user);
    }

    public void deleteUser(Long userId) {
        requireAdmin();
        deleteUserUsecase.execute(userId);
    }

    public UserResponse activateUser(Long userId) {
        requireAdmin();
        return userMapper.toResponse(activateUserUsecase.execute(userId));
    }

    public UserResponse desactivateUser(Long userId) {
        requireAdmin();
        return userMapper.toResponse(desactivateUserUsecase.execute(userId));
    }

    private Long getCurrentUserId() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated"));
    }

    private void requireAdmin() {
        if (!currentUserProvider.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can manage users");
        }
    }
}
