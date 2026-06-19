package cm.bognestanley.shop_backend.application.cart.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.entity.CartItem;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository; 

@Service
public class MergeCartUsecase {

    private final CartRepository cartRepository;

    public MergeCartUsecase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void execute(Long userId, Long guestCartId){
        Cart guestCart = cartRepository.findById(guestCartId).orElse(null);

        if (guestCart == null) {
            return;
        }

        Cart userCart = cartRepository.findByUserId(userId).orElseGet(() -> Cart.create(userId));

        for (CartItem item : guestCart.getCartItems()) {
            userCart.addProductVariant(item.getProduct(), item.getProductVariant(), item.getQuantity());
        }

        cartRepository.save(userCart);
        cartRepository.delete(guestCart);
    }
    
}
