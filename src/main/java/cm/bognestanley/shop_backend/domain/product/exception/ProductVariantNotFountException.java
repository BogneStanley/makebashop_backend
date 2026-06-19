package cm.bognestanley.shop_backend.domain.product.exception;

public class ProductVariantNotFountException extends RuntimeException {
    
    public ProductVariantNotFountException(String message) {
        super(message);
    }

    public ProductVariantNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductVariantNotFountException() {
    }
}
