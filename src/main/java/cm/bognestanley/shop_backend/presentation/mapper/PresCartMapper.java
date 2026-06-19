package cm.bognestanley.shop_backend.presentation.mapper;

import java.util.ArrayList;
import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.cart.dto.AddToCartCommand;
import cm.bognestanley.shop_backend.application.cart.dto.UpdateCartCommand;
import cm.bognestanley.shop_backend.application.cart.dto.UpdateVariantQuantityCommand;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.entity.CartItem;
import cm.bognestanley.shop_backend.presentation.dto.request.cart.AddToCartRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.cart.UpdateCartRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.cart.UpdateVariantQuantityRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.cart.CartItemResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.cart.CartResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductVariantResponse;

@Component
public class PresCartMapper {

    private final PresProductMapper productMapper;

    public PresCartMapper(PresProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public AddToCartCommand toAddToCartCommand(AddToCartRequest request) {

        return new AddToCartCommand(
                request.productId(),
                request.variantId(),
                request.quantity());
    }

    public UpdateCartCommand toUpdateCartCommand(UpdateCartRequest request) {
        return new UpdateCartCommand(
                request.variantsQuantity().stream()
                        .map(this::toUpdateVariantQuantityCommand)
                        .toList());
    }

    public UpdateVariantQuantityCommand toUpdateVariantQuantityCommand(UpdateVariantQuantityRequest request) {
        return new UpdateVariantQuantityCommand(
                request.variantId(),
                request.quantity());
    }

    public CartItemResponse toCartItemResponse(CartItem cartItem) {

        return new CartItemResponse(
                cartItem.getId(),
                productMapper.toResponse(cartItem.getProduct()).copyWith(new ArrayList<ProductVariantResponse>()),
                productMapper.toVariantResponse(cartItem.getProductVariant()),
                cartItem.getQuantity(),
                new MoneyResponse(cartItem.getSubtotal().amount(), cartItem.getSubtotal().currency()));
    }

    public CartResponse toCartResponse(Cart cart) {
        MoneyResponse moneyResponse;
        if (cart.getTotalAmount() == null) {
            moneyResponse = null;
        } else {
            moneyResponse = new MoneyResponse(cart.getTotalAmount().amount(), cart.getTotalAmount().currency());
        }
        return new CartResponse(
                cart.getId(),
                moneyResponse,
                cart.getCartItems().stream().map(this::toCartItemResponse).toList());
    }

}
