package cm.bognestanley.shop_backend.infrastructure.cart;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.infrastructure.security.CurrentUserProvider;

@Component
public class CartResolverAdapter implements CartResolver {

    private final CurrentUserProvider currentUserProvider;
    private final HeaderCartContextProvider headerCartContextProvider;
    private final CartRepository cartRepository;

    public CartResolverAdapter(CurrentUserProvider currentUserProvider, HeaderCartContextProvider headerCartContextProvider, CartRepository cartRepository) {
        this.currentUserProvider = currentUserProvider;
        this.headerCartContextProvider = headerCartContextProvider;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart resolveCart() {
        if(currentUserProvider.getCurrentUserId().isPresent()){
            return cartRepository.findByUserId(currentUserProvider.getCurrentUserId().get())
                    .orElseGet(() -> createNewCart(currentUserProvider.getCurrentUserId().get()));
        }
        else if(headerCartContextProvider.getCartIdFromHeader().isPresent()){
            Cart cart = cartRepository.findById(headerCartContextProvider.getCartIdFromHeader().get())
                    .orElseGet(() -> createNewCart(null));
            
            if(cart.getUserId() != null){
                throw new AccessDeniedException("You can't access this cart");
            }

            return cart;
        }
        return createNewCart(null);
        
    }

    @Override
    public Cart resolveCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new DomainErrorException(ErrorCode.CART_NOT_FOUND, "Cart not found"));
    }

    private Cart createNewCart(Long userId) {
        return Cart.create(userId);
    }
    
}
