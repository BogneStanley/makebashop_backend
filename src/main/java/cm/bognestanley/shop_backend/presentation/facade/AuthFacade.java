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
import cm.bognestanley.shop_backend.presentation.dto.response.user.AuthResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final RegisterUserUsecase registerUserUsecase;
    private final LoginUsecase loginUsecase;
    private final JwtUtils jwtUtils;

    public AuthResponse register(RegisterUserRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.avatar(),
                cm.bognestanley.shop_backend.domain.user.entity.UserRole.CUSTOMER
        );

        User user = registerUserUsecase.execute(command);
        String token = jwtUtils.generateToken(UserDetailsImpl.from(user));

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getRole(),
                token
        );
    }

    public AuthResponse login(LoginRequest request) {
        LoginCommand command = new LoginCommand(
                request.email(),
                request.password()
        );

        User user = loginUsecase.execute(command);
        String token = jwtUtils.generateToken(UserDetailsImpl.from(user));

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatar(),
                user.getRole(),
                token
        );
    }
}
