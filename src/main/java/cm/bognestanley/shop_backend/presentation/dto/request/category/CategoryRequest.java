package cm.bognestanley.shop_backend.presentation.dto.request.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Category name cannot be null")
        String name,
        String description
) {
}
