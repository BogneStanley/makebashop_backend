package cm.bognestanley.shop_backend.domain.cart.entity;

import java.util.ArrayList;
import java.util.List;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;

public class Cart {
    private Long id;
    private Long userId;
    private List<CartItem> cartItems;

    public Cart(Long id, Long userId, List<CartItem> cartItems) {
        this.id = id;
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public static Cart create() {
        return new Cart(null, null, new ArrayList<>());
    }

    public static Cart create(Long userId) {
        return new Cart(null, userId, new ArrayList<>());
    }

    public void addProductVariant(Product product, ProductVariant variant, int quantity) {
        if (isProductInCart(variant)) {
            this.cartItems.stream()
                .filter(cartItem -> cartItem.getProductVariant().getId().equals(variant.getId()))
                .findFirst()
                .ifPresent(cartItem -> cartItem.increaseQuantity(quantity));
        } else {
            this.cartItems.add(CartItem.create(product, variant, quantity));
        }
    }

    public void removeProductVariant(Long variantId) {
        this.cartItems.removeIf(cartItem -> cartItem.getProductVariant().getId() == variantId);
    }

    public void updateQuantity(Long variantId, int quantity) {
        this.cartItems.stream()
                .filter(cartItem -> cartItem.getProductVariant().getId().equals(variantId))
                .findFirst()
                .ifPresent(cartItem -> cartItem.updateQuantity(quantity));
    }

    public void clearCart() {
        this.cartItems.clear();
    }

    public Money getTotalAmount() {
        return this.cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(Money::add)
                .orElse(null);
    }

    private boolean isProductInCart(ProductVariant variant) {
        return this.cartItems.stream()
                .anyMatch(cartItem -> cartItem.getProductVariant().getId().equals(variant.getId()));
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Long getUserId() {
        return userId;
    }
}
