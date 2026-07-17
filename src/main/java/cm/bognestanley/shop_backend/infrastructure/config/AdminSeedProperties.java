package cm.bognestanley.shop_backend.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.admin.seed")
public class AdminSeedProperties {

    private boolean enabled = true;
    private String email = "admin@shop.local";
    private String password = "Admin@123456";
    private String firstName = "Admin";
    private String lastName = "User";
}
