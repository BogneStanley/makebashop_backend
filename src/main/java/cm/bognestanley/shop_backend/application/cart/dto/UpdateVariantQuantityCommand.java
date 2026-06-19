package cm.bognestanley.shop_backend.application.cart.dto;

public record UpdateVariantQuantityCommand(
        Long variantId,
        int quantity) {

    public UpdateVariantQuantityCommand {
        if (variantId == null) {
            throw new IllegalArgumentException("Product variant id must be provided");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

}
