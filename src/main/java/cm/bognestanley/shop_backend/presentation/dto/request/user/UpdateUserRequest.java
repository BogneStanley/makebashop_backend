package cm.bognestanley.shop_backend.presentation.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import cm.bognestanley.shop_backend.domain.user.entity.UserRole;

public record UpdateUserRequest(
        @Email(message = "Email must be valid")
        String email,

        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        String firstName,
        String lastName,
        String avatar,
        UserRole role) {
}
