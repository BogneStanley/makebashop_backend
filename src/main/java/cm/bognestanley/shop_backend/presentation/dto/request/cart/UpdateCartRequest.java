package cm.bognestanley.shop_backend.presentation.dto.request.cart;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record UpdateCartRequest(
                @NotEmpty(message = "Variants quantity must be provided") @Valid List<UpdateVariantQuantityRequest> variantsQuantity) {

}
