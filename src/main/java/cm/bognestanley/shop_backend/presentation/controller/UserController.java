package cm.bognestanley.shop_backend.presentation.controller;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.presentation.dto.request.user.CreateUserRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Admin user management endpoints")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    @Operation(summary = "Create a new user (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse userResponse = userFacade.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDataWrapper.ok(userResponse, "USER_CREATED", "User created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all users (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<PaginatedEntity<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        PaginationAttribute attribute = new PaginationAttribute(
                page,
                size,
                new SortEntity(sortBy, sortOrder)
        );

        PaginatedEntity<UserResponse> users = userFacade.getAllUsers(attribute);
        return ResponseEntity.ok(ResponseDataWrapper.ok(users, "USERS_RETRIEVED", "Users retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userFacade.updateUser(id, request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(userResponse, "USER_UPDATED", "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<Void>> deleteUser(@PathVariable Long id) {
        userFacade.deleteUser(id);
        return ResponseEntity.ok(ResponseDataWrapper.ok(null, "USER_DELETED", "User deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate user by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> activateUser(@PathVariable Long id) {
        UserResponse userResponse = userFacade.activateUser(id);
        return ResponseEntity.ok(ResponseDataWrapper.ok(userResponse, "USER_ACTIVATED", "User activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> deactivateUser(@PathVariable Long id) {
        UserResponse userResponse = userFacade.desactivateUser(id);
        return ResponseEntity.ok(ResponseDataWrapper.ok(userResponse, "USER_DEACTIVATED", "User deactivated successfully"));
    }
}
