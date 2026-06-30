package cm.bognestanley.shop_backend.infrastructure.security;

public final class AuthMode {

    public static final String HEADER = "X-Auth-Mode";
    public static final String COOKIE = "cookie";
    public static final String BEARER = "bearer";

    private AuthMode() {
    }

    public static boolean isCookieMode(String headerValue) {
        return COOKIE.equalsIgnoreCase(headerValue);
    }
}
