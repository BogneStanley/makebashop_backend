package cm.bognestanley.shop_backend.presentation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.infrastructure.security.AuthCookieService;
import cm.bognestanley.shop_backend.infrastructure.security.AuthMode;
import cm.bognestanley.shop_backend.presentation.dto.request.user.LoginRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.user.RegisterUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.user.AuthResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.user.AuthenticatedSession;
import cm.bognestanley.shop_backend.presentation.facade.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and authorization")
public class AuthController {

    private final AuthFacade authFacade;
    private final AuthCookieService authCookieService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<AuthResponse>> register(
            @Valid @RequestBody RegisterUserRequest request,
            @Parameter(description = "Use 'cookie' for browser (HttpOnly cookie), 'bearer' for mobile (token in body)")
            @RequestHeader(value = AuthMode.HEADER, defaultValue = AuthMode.BEARER) String authMode) {
        AuthenticatedSession session = authFacade.register(request);
        return buildAuthResponse(session, authMode, HttpStatus.CREATED, "USER_REGISTERED", "User registered successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            @Parameter(description = "Use 'cookie' for browser (HttpOnly cookie), 'bearer' for mobile (token in body)")
            @RequestHeader(value = AuthMode.HEADER, defaultValue = AuthMode.BEARER) String authMode) {
        AuthenticatedSession session = authFacade.login(request);
        return buildAuthResponse(session, authMode, HttpStatus.OK, "USER_LOGGED_IN", "User logged in successfully");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user (clears auth cookie)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully")
    })
    public ResponseEntity<ResponseDataWrapper<Void>> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookieService.clearAuthCookie().toString())
                .body(ResponseDataWrapper.ok(null, "USER_LOGGED_OUT", "User logged out successfully"));
    }

    private ResponseEntity<ResponseDataWrapper<AuthResponse>> buildAuthResponse(
            AuthenticatedSession session,
            String authMode,
            HttpStatus status,
            String messageCode,
            String message) {
        boolean cookieMode = AuthMode.isCookieMode(authMode);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status);

        if (cookieMode) {
            builder.header(HttpHeaders.SET_COOKIE, authCookieService.createAuthCookie(session.accessToken()).toString());
        }

        return builder.body(ResponseDataWrapper.ok(session.toAuthResponse(cookieMode), messageCode, message));
    }
}
