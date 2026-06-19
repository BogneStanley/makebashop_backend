package cm.bognestanley.shop_backend.application.cart.dto;

public record AddToCartCommand(
    Long productId,
    Long variantId,
    int quantity
) {
    
    public AddToCartCommand {
        if (variantId == null || productId == null) {
            throw new IllegalArgumentException("Product variant id and product id must be provided");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }
    
}
