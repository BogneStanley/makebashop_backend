package cm.bognestanley.shop_backend.domain.common.exception;

public enum ErrorCode {
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found"),
    PRODUCT_VARIANT_NOT_FOUND("PRODUCT_VARIANT_NOT_FOUND", "Product variant not found"),
    CART_NOT_FOUND("CART_NOT_FOUND", "Cart not found"),
    IMAGE_NOT_FOUND("IMAGE_NOT_FOUND", "Image not found"),
    QUANTITY_SHOULD_BE_POSITIVE("QUANTITY_SHOULD_BE_POSITIVE", "Quantity should be positive"),
    INVALID_ORDER_SWITCH_STATUS("INVALID_ORDER_SWITCH_STATUS", "Invalid order switch status"),
    OUT_OF_STOCK("OUT_OF_STOCK", "Out of stock"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "Order not found");
    
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
