package cm.bognestanley.shop_backend.application.cart.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.cart.dto.UpdateCartCommand;
import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository;

@Service
public class UpdateCartUsecase {

    private final CartRepository cartRepository;
    private final CartResolver cartResolver;

    public UpdateCartUsecase(CartRepository cartRepository, CartResolver cartResolver) {
        this.cartRepository = cartRepository;
        this.cartResolver = cartResolver;
    }

    public Cart execute(UpdateCartCommand command) {
        Cart cart = cartResolver.resolveCart();

        command.variantCommands()
                .forEach(variantCommand -> cart.updateQuantity(variantCommand.variantId(), variantCommand.quantity()));

        return cartRepository.save(cart);
    }

}
