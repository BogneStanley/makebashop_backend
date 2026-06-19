package cm.bognestanley.shop_backend.application.cart.dto;

import java.util.List;

public record UpdateCartCommand(
    List<UpdateVariantQuantityCommand> variantCommands
) {

    public UpdateCartCommand {
        if (variantCommands == null || variantCommands.isEmpty()) {
            throw new IllegalArgumentException("Variant commands must be provided");
        }
    }
    
}
