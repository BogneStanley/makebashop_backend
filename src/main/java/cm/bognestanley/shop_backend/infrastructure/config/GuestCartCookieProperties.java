package cm.bognestanley.shop_backend.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cart.cookie")
public class GuestCartCookieProperties {

    private String name = "guest_cart";
    private boolean secure = false;
    private String sameSite = "Lax";
    private String domain;
    private String path = "/";
    private long maxAgeDays = 30;
}
