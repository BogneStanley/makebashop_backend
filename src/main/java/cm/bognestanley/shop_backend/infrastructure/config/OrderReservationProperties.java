package cm.bognestanley.shop_backend.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.orders")
public class OrderReservationProperties {
    private long reservationDurationMinutes = 60;
}
