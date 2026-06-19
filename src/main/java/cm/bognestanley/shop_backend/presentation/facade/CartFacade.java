package cm.bognestanley.shop_backend.presentation.facade;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.cart.usecase.AddProductToCartUsecase;
import cm.bognestanley.shop_backend.application.cart.usecase.ClearCartUsecase;
import cm.bognestanley.shop_backend.application.cart.usecase.GetCartUsecase;
import cm.bognestanley.shop_backend.application.cart.usecase.RemoveVariantToCartUsecase;
import cm.bognestanley.shop_backend.application.cart.usecase.UpdateCartUsecase;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.presentation.dto.request.cart.AddToCartRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.cart.UpdateCartRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.cart.CartResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresCartMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartFacade {
    private final AddProductToCartUsecase addProductToCartUsecase;
    private final UpdateCartUsecase updateCartUsecase;
    private final GetCartUsecase getCartUsecase;
    private final RemoveVariantToCartUsecase removeVariantToCartUsecase;
    private final ClearCartUsecase clearCartUsecase;
    private final PresCartMapper cartMapper;

    public CartResponse addToCart(AddToCartRequest request) {

        Cart cart = addProductToCartUsecase.execute(cartMapper.toAddToCartCommand(request));
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse updateCart(UpdateCartRequest request) {
        Cart cart = updateCartUsecase.execute(cartMapper.toUpdateCartCommand(request));
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse getCart() {
        Cart cart = getCartUsecase.execute();
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse removeVariantToCart(Long variantId) {
        Cart cart = removeVariantToCartUsecase.execute(variantId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse clearCart() {
        Cart cart = clearCartUsecase.execute();
        return cartMapper.toCartResponse(cart);
    }
}
