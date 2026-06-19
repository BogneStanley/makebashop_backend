package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record DeleteImagesRequest(
        @NotEmpty(message = "At least one image ID is required") List<Long> imageIds) {

}
