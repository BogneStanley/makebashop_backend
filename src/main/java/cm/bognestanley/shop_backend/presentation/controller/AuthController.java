package cm.bognestanley.shop_backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.presentation.dto.request.user.LoginRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.user.RegisterUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.user.AuthResponse;
import cm.bognestanley.shop_backend.presentation.facade.AuthFacade;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<AuthResponse>> register(
            @Valid @RequestBody RegisterUserRequest request) {
        AuthResponse authResponse = authFacade.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDataWrapper.ok(authResponse, "USER_REGISTERED", "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authFacade.login(request);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(authResponse, "USER_LOGGED_IN", "User logged in successfully"));
    }
}
