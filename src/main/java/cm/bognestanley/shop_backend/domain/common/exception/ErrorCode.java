package cm.bognestanley.shop_backend.domain.common.exception;

public enum ErrorCode {
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found", 404),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found", 404),
    USER_ALREADY_EXIST("USER_ALREADY_EXIST", "User already exist", 409),
    PRODUCT_VARIANT_NOT_FOUND("PRODUCT_VARIANT_NOT_FOUND", "Product variant not found", 404),
    PRODUCT_VARIANT_SKU_ALREADY_EXIST("PRODUCT_VARIANT_SKU_ALREADY_EXIST", "Product variant with same SKU already exist", 409),
    CART_NOT_FOUND("CART_NOT_FOUND", "Cart not found", 404),
    IMAGE_NOT_FOUND("IMAGE_NOT_FOUND", "Image not found", 404),
    QUANTITY_SHOULD_BE_POSITIVE("QUANTITY_SHOULD_BE_POSITIVE", "Quantity should be positive", 400),
    PRODUCT_MUST_HAVE_AT_LEAST_ONE_VARIANT("PRODUCT_MUST_HAVE_AT_LEAST_ONE_VARIANT", "Product must have at least one variant", 400),
    INVALID_ORDER_SWITCH_STATUS("INVALID_ORDER_SWITCH_STATUS", "Invalid order switch status", 400),
    ALL_PRODUCT_VARIANT_MUST_HAVE_SAME_CURRENCY("ALL_PRODUCT_VARIANT_MUST_HAVE_SAME_CURRENCY", "All product variant must have same currency", 400),
    OUT_OF_STOCK("OUT_OF_STOCK", "Out of stock", 400),
    INVALID_INPUT("INVALID_INPUT", "Invalid input", 400),
    AMOUNT_CANNOT_BE_NEGATIVE("AMOUNT_CANNOT_BE_NEGATIVE", "Amount cannot be negative", 400),
    CURRENCY_CANNOT_BE_NULL("CURRENCY_CANNOT_BE_NULL", "Currency cannot be null", 400),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "Order not found", 404),
    CURRENCY_MISMATCH("CURRENCY_MISMATCH", "Currency mismatch", 400);
    
    private final String code;
    private final String message;
    private final Integer httpStatusCode;

    ErrorCode(String code, String message, Integer httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
}
