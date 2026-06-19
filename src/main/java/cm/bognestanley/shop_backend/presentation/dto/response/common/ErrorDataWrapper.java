package cm.bognestanley.shop_backend.presentation.dto.response.common;

public record ErrorDataWrapper<T>(T errors, String messageCode, String error) {

    public static <T> ErrorDataWrapper<T> error(T errors, String messageCode, String error) {
        return new ErrorDataWrapper<>(errors, messageCode, error);
    }

    public static <T> ErrorDataWrapper<T> error(T errors, String messageCode) {
        return new ErrorDataWrapper<>(errors, messageCode, null);
    }

    public static <T> ErrorDataWrapper<T> error(T errors) {
        return new ErrorDataWrapper<>(errors, "ERROR_UNKNOWN", "Unknown error");
    }

    public static <T> ErrorDataWrapper<T> empty() {
        return new ErrorDataWrapper<>(null, "ERROR_UNKNOWN", "Unknown error");
    }

}
