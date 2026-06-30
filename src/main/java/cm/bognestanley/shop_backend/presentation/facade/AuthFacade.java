package cm.bognestanley.shop_backend.presentation.facade;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.user.dto.LoginCommand;
import cm.bognestanley.shop_backend.application.user.dto.RegisterUserCommand;
import cm.bognestanley.shop_backend.application.user.usecase.LoginUsecase;
import cm.bognestanley.shop_backend.application.user.usecase.RegisterUserUsecase;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.infrastructure.security.JwtUtils;
import cm.bognestanley.shop_backend.infrastructure.security.UserDetailsImpl;
import cm.bognestanley.shop_backend.presentation.dto.request.user.LoginRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.user.RegisterUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.user.AuthenticatedSession;
import cm.bognestanley.shop_backend.presentation.dto.response.user.UserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthFacade {

        private final RegisterUserUsecase registerUserUsecase;
        private final LoginUsecase loginUsecase;
        private final JwtUtils jwtUtils;

        public AuthenticatedSession register(RegisterUserRequest request) {
                RegisterUserCommand command = new RegisterUserCommand(
                                request.email(),
                                request.password(),
                                request.firstName(),
                                request.lastName(),
                                request.avatar(),
                                cm.bognestanley.shop_backend.domain.user.entity.UserRole.CUSTOMER);

                User user = registerUserUsecase.execute(command);
                return buildSession(user);
        }

        public AuthenticatedSession login(LoginRequest request) {
                LoginCommand command = new LoginCommand(
                                request.email(),
                                request.password());

                User user = loginUsecase.execute(command);
                return buildSession(user);
        }

        private AuthenticatedSession buildSession(User user) {
                String token = jwtUtils.generateToken(UserDetailsImpl.from(user));

                return new AuthenticatedSession(
                                new UserResponse(
                                                user.getId(),
                                                user.getEmail(),
                                                user.getFirstName(),
                                                user.getLastName(),
                                                user.getAvatar(),
                                                user.getRole(),
                                                user.isActivate(),
                                                user.getCreatedAt(),
                                                user.getUpdatedAt()),
                                token);
        }
}
