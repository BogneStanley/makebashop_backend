package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderLineItemJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderStatusJpa;

public interface OrderLineItemJpaRepository extends JpaRepository<OrderLineItemJpaEntity, Long> {

    @Query("""
            SELECT oi.product.id
            FROM OrderLineItemJpaEntity oi
            WHERE oi.order.status = :status
              AND oi.product.isActive = true
            GROUP BY oi.product.id
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<Long> findMostPopularProductIds(@Param("status") OrderStatusJpa status, Pageable pageable);

}
