package cm.bognestanley.shop_backend.domain.order.criteria;

import java.time.LocalDateTime;

public record OrderSearchCriteria(
        String orderNumber,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate) {

}
