package cm.bognestanley.shop_backend.presentation.dto.request.product;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record UpdateImagesPositionRequest(
        @Valid @NotEmpty List<UpdateImagePositionRequest> imagesPosition) {

}
