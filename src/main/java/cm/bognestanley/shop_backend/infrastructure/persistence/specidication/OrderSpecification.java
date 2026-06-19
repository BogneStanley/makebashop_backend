package cm.bognestanley.shop_backend.infrastructure.persistence.specidication;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import cm.bognestanley.shop_backend.domain.order.criteria.OrderSearchCriteria;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderJpaEntity;

public class OrderSpecification {

    public static Specification<OrderJpaEntity> byRef(String orderNumber) {
        return (root, query, criteriaBuilder) -> {
            if (orderNumber == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("orderNumber"), orderNumber);
        };
    }

    public static Specification<OrderJpaEntity> byStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<OrderJpaEntity> byCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            }
            if (startDate == null && endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
            }
            return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
        };
    }

    public static Specification<OrderJpaEntity> toSpecification(OrderSearchCriteria criteria) {
        return Specification.where(byRef(criteria.orderNumber()))
                .and(byStatus(criteria.status()))
                .and(byCreatedAtBetween(criteria.startDate(), criteria.endDate()));
    }
}
