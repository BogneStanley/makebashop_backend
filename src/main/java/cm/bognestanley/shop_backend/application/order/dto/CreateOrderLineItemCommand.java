package cm.bognestanley.shop_backend.application.order.dto;

public record CreateOrderLineItemCommand(Long productId, Long productVariantId, int quantity) {
}
