package cm.bognestanley.shop_backend.presentation.dto.response.product;

import java.io.Serializable;

public record ProductImageResponse(
    Long id,
    String url,
    String name,
    Integer position,
    Boolean isPrimary
) implements Serializable {
}
