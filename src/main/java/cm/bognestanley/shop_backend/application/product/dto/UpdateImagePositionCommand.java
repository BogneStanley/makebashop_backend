package cm.bognestanley.shop_backend.application.product.dto;

public record UpdateImagePositionCommand(
    Long imageId,
    Integer newPosition
) {
    
}
