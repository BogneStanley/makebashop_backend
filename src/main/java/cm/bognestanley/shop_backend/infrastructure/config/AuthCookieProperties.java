package cm.bognestanley.shop_backend.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.auth.cookie")
public class AuthCookieProperties {

    private String name = "access_token";
    private boolean secure = false;
    private String sameSite = "Lax";
    private String domain;
    private String path = "/";
}
