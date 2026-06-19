package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateProductRequest(
        @NotBlank(message = "Product name is required") String name,

        @NotBlank(message = "Product description is required") String description,

        @NotEmpty(message = "Product variants are required") @Valid List<ProductVariantRequest> productVariants) {

}
