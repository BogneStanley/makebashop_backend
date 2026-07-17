package cm.bognestanley.shop_backend.presentation.dto.request.user;

import jakarta.validation.constraints.Email;

public record UpdateProfileRequest(
        @Email(message = "Email must be valid")
        String email,

        String firstName,
        String lastName,
        String avatar) {
}
