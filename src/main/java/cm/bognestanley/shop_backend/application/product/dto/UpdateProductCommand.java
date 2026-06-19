package cm.bognestanley.shop_backend.application.product.dto;

public record UpdateProductCommand(
    Long id,
    String name,
    String description
) {}
