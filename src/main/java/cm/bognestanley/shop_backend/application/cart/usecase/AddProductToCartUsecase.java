package cm.bognestanley.shop_backend.application.cart.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.cart.dto.AddToCartCommand;
import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.cart.repository.CartRepository;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class AddProductToCartUsecase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartResolver cartResolver;

    public AddProductToCartUsecase(CartRepository cartRepository, ProductRepository productRepository,
            CartResolver cartResolver) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartResolver = cartResolver;
    }

    public Cart execute(AddToCartCommand command) {

        Cart cart = cartResolver.resolveCart();

        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new DomainErrorException(ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with ID: " + command.productId()));

        ProductVariant variant = product.getProductVariants().stream()
                .filter(v -> v.getId().equals(command.variantId())).findFirst()
                .orElseThrow(() -> new DomainErrorException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                        "Product variant not found with ID: " + command.variantId()));

        cart.addProductVariant(product, variant, command.quantity());

        return cartRepository.save(cart);
    }

}
