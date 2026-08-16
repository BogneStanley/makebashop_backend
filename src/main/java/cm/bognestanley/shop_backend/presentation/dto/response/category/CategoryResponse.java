package cm.bognestanley.shop_backend.presentation.dto.response.category;

import java.io.Serializable;

public record CategoryResponse(
        Long id,
        String name,
        String description
) implements Serializable {
}
