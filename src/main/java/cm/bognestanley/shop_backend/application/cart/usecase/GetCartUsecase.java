package cm.bognestanley.shop_backend.application.cart.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;

@Service
public class GetCartUsecase {

    private final CartResolver cartResolver;

    public GetCartUsecase(CartResolver cartResolver) {
        this.cartResolver = cartResolver;
    }

    public Cart execute() {
        return cartResolver.resolveCart();
    }

}
