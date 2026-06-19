package cm.bognestanley.shop_backend.domain.product.exception;

public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException() {
    }
}
