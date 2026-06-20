package cm.bognestanley.shop_backend.application.cart.dto;

public record AddToCartCommand(
    Long productId,
    Long variantId,
    int quantity
) {
    
}
