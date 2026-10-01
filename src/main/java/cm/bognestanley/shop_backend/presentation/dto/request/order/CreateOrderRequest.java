package cm.bognestanley.shop_backend.presentation.dto.request.order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 80, message = "First name must not exceed 80 characters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(max = 80, message = "Last name must not exceed 80 characters")
        String lastName,
        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String email,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9 ()-]{8,20}$", message = "Phone number format is invalid")
        String phoneNumber,
        @Size(max = 1000, message = "Note must not exceed 1000 characters")
        String note) {
}
