package cm.bognestanley.shop_backend.presentation.dto.response.common;

public record ResponseDataWrapper<T>(T data, String messageCode, String message) {

    public static <T> ResponseDataWrapper<T> ok(T data, String messageCode, String message) {
        return new ResponseDataWrapper<>(data, messageCode, message);
    }

    public static <T> ResponseDataWrapper<T> ok(T data, String messageCode) {
        return new ResponseDataWrapper<>(data, messageCode, "Operation successful");
    }

    public static <T> ResponseDataWrapper<T> ok(T data) {
        return new ResponseDataWrapper<>(data, "SUCCESS", "Operation successful");
    }

    public static <T> ResponseDataWrapper<T> ok() {
        return new ResponseDataWrapper<>(null, "SUCCESS", "Operation successful");
    }

}
