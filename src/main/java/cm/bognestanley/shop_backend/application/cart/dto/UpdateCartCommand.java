package cm.bognestanley.shop_backend.application.cart.dto;

import java.util.List;

public record UpdateCartCommand(
        List<UpdateVariantQuantityCommand> variantCommands) {
}
