package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record DeleteVariantsRequest(
        @NotEmpty(message = "At least one variant ID is required") List<Long> variantIds) {

}
