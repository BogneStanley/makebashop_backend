package cm.bognestanley.shop_backend.application.cart.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository;


@Service
public class RemoveVariantToCartUsecase {

    private final CartRepository cartRepository;
    private final CartResolver cartResolver;

    public RemoveVariantToCartUsecase(CartRepository cartRepository, CartResolver cartResolver) {
        this.cartRepository = cartRepository;
        this.cartResolver = cartResolver;
    }


    public Cart execute(Long variantId) {
        Cart cart = cartResolver.resolveCart();

        cart.removeProductVariant(variantId);

        return cartRepository.save(cart);
    }

}
