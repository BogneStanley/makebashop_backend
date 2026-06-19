package cm.bognestanley.shop_backend.domain.user.exception;


public class BadUserCredentials extends RuntimeException {
    public BadUserCredentials(String message, Throwable ex) {
        super(message, ex);
    }

    public BadUserCredentials(String message) {
        super(message);
    }

    public BadUserCredentials() {
    }
}
