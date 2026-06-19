package cm.bognestanley.shop_backend.presentation.controller;

import cm.bognestanley.shop_backend.presentation.dto.request.user.UpdateUserRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.user.UserResponse;
import cm.bognestanley.shop_backend.presentation.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Authenticated user profile endpoints")
public class UserProfileController {

    private final UserFacade userFacade;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> getProfile() {
        UserResponse userResponse = userFacade.getProfile();
        return ResponseEntity.ok(ResponseDataWrapper.ok(userResponse, "PROFILE_RETRIEVED", "Profile retrieved successfully"));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userFacade.updateProfile(request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(userResponse, "PROFILE_UPDATED", "Profile updated successfully"));
    }
}
