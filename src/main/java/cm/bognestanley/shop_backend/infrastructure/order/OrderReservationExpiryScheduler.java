package cm.bognestanley.shop_backend.infrastructure.order;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.order.usecase.ExpirePendingOrderReservationsUsecase;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderReservationExpiryScheduler {

    private final ExpirePendingOrderReservationsUsecase expirePendingOrders;

    public OrderReservationExpiryScheduler(ExpirePendingOrderReservationsUsecase expirePendingOrders) {
        this.expirePendingOrders = expirePendingOrders;
    }

    @Scheduled(fixedDelayString = "${app.orders.reservation-expiration-scan-interval-ms:60000}")
    public void releaseExpiredReservations() {
        int count = expirePendingOrders.execute(LocalDateTime.now());
        if (count > 0) {
            log.info("Released inventory for {} expired pending orders", count);
        }
    }
}
