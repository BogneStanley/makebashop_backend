package cm.bognestanley.shop_backend.infrastructure.security;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.infrastructure.config.AuthCookieProperties;

@Service
public class AuthCookieService {

    private final AuthCookieProperties cookieProperties;
    private final long jwtExpirationMs;

    public AuthCookieService(
            AuthCookieProperties cookieProperties,
            @Value("${jwt.expiration}") long jwtExpirationMs) {
        this.cookieProperties = cookieProperties;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String getCookieName() {
        return cookieProperties.getName();
    }

    public ResponseCookie createAuthCookie(String token) {
        return buildCookie(token, Duration.ofMillis(jwtExpirationMs));
    }

    public ResponseCookie clearAuthCookie() {
        return buildCookie("", Duration.ZERO);
    }

    private ResponseCookie buildCookie(String value, Duration maxAge) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(cookieProperties.getName(), value)
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path(cookieProperties.getPath())
                .maxAge(maxAge)
                .sameSite(cookieProperties.getSameSite());

        if (cookieProperties.getDomain() != null && !cookieProperties.getDomain().isBlank()) {
            builder.domain(cookieProperties.getDomain());
        }

        return builder.build();
    }
}
