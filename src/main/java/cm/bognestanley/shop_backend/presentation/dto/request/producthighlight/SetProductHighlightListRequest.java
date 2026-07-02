package cm.bognestanley.shop_backend.presentation.dto.request.producthighlight;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SetProductHighlightListRequest(
        @NotNull @Size(max = 4) List<Long> productIds) {
}
