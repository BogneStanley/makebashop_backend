package cm.bognestanley.shop_backend.application.product.dto;

import java.util.List;

public record UpdateProductCommand(
    Long id,
    String name,
    String description,
    Boolean isActive,
    List<Long> categoryIds
) {}
