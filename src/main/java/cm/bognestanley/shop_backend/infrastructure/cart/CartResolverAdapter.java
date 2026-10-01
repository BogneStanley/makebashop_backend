package cm.bognestanley.shop_backend.infrastructure.cart;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.infrastructure.security.CurrentUserProvider;

@Component
public class CartResolverAdapter implements CartResolver {

    private final CurrentUserProvider currentUserProvider;
    private final GuestCartCookieService guestCartCookieService;
    private final CartRepository cartRepository;

    public CartResolverAdapter(CurrentUserProvider currentUserProvider, GuestCartCookieService guestCartCookieService, CartRepository cartRepository) {
        this.currentUserProvider = currentUserProvider;
        this.guestCartCookieService = guestCartCookieService;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart resolveCart() {
        var currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId.isPresent()) {
            return cartRepository.findByUserId(currentUserId.get())
                    .orElseGet(() -> Cart.create(currentUserId.get()));
        }
        ServletRequestAttributes attributes = currentRequestAttributes();
        var guestToken = guestCartCookieService.getToken(attributes.getRequest());
        if (guestToken.isPresent()) {
            return cartRepository.findByGuestToken(guestToken.get())
                    .orElseThrow(() -> {
                        guestCartCookieService.clearToken(attributes.getResponse());
                        return new DomainErrorException(ErrorCode.CART_NOT_FOUND, "Guest cart not found");
                    });
        }

        Cart cart = Cart.createGuest(guestCartCookieService.generateToken());
        Cart savedCart = cartRepository.save(cart);
        guestCartCookieService.writeToken(attributes.getResponse(), savedCart.getGuestToken());
        return savedCart;
        
    }

    private ServletRequestAttributes currentRequestAttributes() {
        Object attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes servletAttributes)
                || servletAttributes.getResponse() == null) {
            throw new IllegalStateException("Guest carts require an active HTTP request");
        }
        return servletAttributes;
    }
    
}
