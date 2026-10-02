package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderStatusJpa;
import jakarta.persistence.LockModeType;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long>, JpaSpecificationExecutor<OrderJpaEntity> {
    @EntityGraph(attributePaths = {"orderLineItems", "orderLineItems.product", "orderLineItems.productVariant"})
    Optional<OrderJpaEntity> findByIdempotencyKey(String idempotencyKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select distinct order from OrderJpaEntity order
            left join fetch order.orderLineItems item
            left join fetch item.product
            left join fetch item.productVariant
            where order.status = :status and order.reservationExpiresAt <= :now
            """)
    List<OrderJpaEntity> findExpiredPendingReservationsForUpdate(
            @Param("status") OrderStatusJpa status, @Param("now") LocalDateTime now);

    @Query("select coalesce(sum(item.price * item.quantity), 0) from OrderLineItemJpaEntity item where item.order.status = :status")
    BigDecimal sumTotalByOrderStatus(@Param("status") OrderStatusJpa status);

    @Query("select count(distinct o.customerPhoneNumber) from OrderJpaEntity o where o.status <> :status")
    long countDistinctCustomersByNonStatus(@Param("status") OrderStatusJpa status);
}
