package cm.bognestanley.shop_backend.application.cart.dto;

public record UpdateVariantQuantityCommand(
        Long variantId,
        int quantity) {
}
