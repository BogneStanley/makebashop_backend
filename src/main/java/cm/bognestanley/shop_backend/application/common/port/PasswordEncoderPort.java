package cm.bognestanley.shop_backend.application.common.port;

public interface PasswordEncoderPort {
    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
