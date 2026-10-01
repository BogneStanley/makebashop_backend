package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.entity.OrderLineItem;
import cm.bognestanley.shop_backend.domain.order.valueObject.Customer;
import cm.bognestanley.shop_backend.domain.order.valueObject.OrderStatus;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderLineItemJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderStatusJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderMapper {

    private final ProductMapper productMapper;
    private final EntityManager entityManager;

    public OrderMapper(ProductMapper productMapper, EntityManager entityManager) {
        this.productMapper = productMapper;
        this.entityManager = entityManager;
    }

    public OrderJpaEntity toJpa(Order order) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerFirstName(order.getCustomer().firstName())
                .customerLastName(order.getCustomer().lastName())
                .customerEmail(order.getCustomer().email())
                .customerPhoneNumber(order.getCustomer().phoneNumber())
                .status(toOrderStatusJpa(order.getStatus()))
                .note(order.getNote())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .cartId(order.getCartId())
                .idempotencyKey(order.getIdempotencyKey())
                .requestFingerprint(order.getRequestFingerprint())
                .reservationExpiresAt(order.getReservationExpiresAt())
                .paidAt(order.getPaidAt())
                .cancelledAt(order.getCancelledAt())
                .orderLineItems(new ArrayList<>())
                .build();

        if (order.getOrderLineItems() != null) {
            for (OrderLineItem item : order.getOrderLineItems()) {
                OrderLineItemJpaEntity orderLineItemJpaEntity = toOrderLineItemJpaEntity(item);
                orderLineItemJpaEntity.setOrder(orderJpaEntity);
                orderJpaEntity.getOrderLineItems().add(orderLineItemJpaEntity);
            }
        }

        return orderJpaEntity;
    }

    public Order toDomain(OrderJpaEntity orderJpaEntity) {
        return new Order(
                orderJpaEntity.getId(),
                orderJpaEntity.getOrderNumber(),
                new Customer(
                        orderJpaEntity.getCustomerFirstName(),
                        orderJpaEntity.getCustomerLastName(),
                        orderJpaEntity.getCustomerEmail(),
                        orderJpaEntity.getCustomerPhoneNumber()),
                orderJpaEntity.getOrderLineItems().stream().map(this::toOrderLineItem)
                        .collect(Collectors.toCollection(ArrayList::new)),
                toOrderStatusDomain(orderJpaEntity.getStatus()),
                orderJpaEntity.getNote(),
                orderJpaEntity.getCreatedAt(),
                orderJpaEntity.getUpdatedAt(),
                orderJpaEntity.getCartId(),
                orderJpaEntity.getIdempotencyKey(),
                orderJpaEntity.getRequestFingerprint(),
                orderJpaEntity.getReservationExpiresAt(),
                orderJpaEntity.getPaidAt(),
                orderJpaEntity.getCancelledAt());
    }

    public OrderLineItemJpaEntity toOrderLineItemJpaEntity(OrderLineItem orderLineItem) {
        ProductJpaEntity productJpaEntity = entityManager.getReference(
                ProductJpaEntity.class, orderLineItem.getProduct().getId());
        ProductVariantJpaEntity productVariantJpaEntity = entityManager.getReference(
                ProductVariantJpaEntity.class, orderLineItem.getProductVariant().getId());

        OrderLineItemJpaEntity orderLineItemJpaEntity = OrderLineItemJpaEntity.builder()
                .id(orderLineItem.getId())
                .quantity(orderLineItem.getQuantity())
                .price(orderLineItem.getPrice().amount())
                .currencyCode(orderLineItem.getPrice().currency())
                .product(productJpaEntity)
                .productVariant(productVariantJpaEntity)
                .build();

        return orderLineItemJpaEntity;
    }

    public OrderLineItem toOrderLineItem(OrderLineItemJpaEntity orderLineItemJpaEntity) {
        return new OrderLineItem(
                orderLineItemJpaEntity.getId(),
                productMapper.toDomain(orderLineItemJpaEntity.getProduct()),
                productMapper.toProductVariantDomain(orderLineItemJpaEntity.getProductVariant()),
                orderLineItemJpaEntity.getQuantity(),
                new Money(orderLineItemJpaEntity.getPrice(), orderLineItemJpaEntity.getCurrencyCode()));
    }

    public OrderStatusJpa toOrderStatusJpa(OrderStatus orderStatus) {
        switch (orderStatus) {
            case OrderStatus.PENDING:
                return OrderStatusJpa.PENDING;
            case OrderStatus.PAID:
                return OrderStatusJpa.PAID;
            case OrderStatus.CANCELLED:
                return OrderStatusJpa.CANCELLED;
            default:
                return null;
        }
    }

    public OrderStatus toOrderStatusDomain(OrderStatusJpa orderStatus) {
        switch (orderStatus) {
            case OrderStatusJpa.PENDING:
                return OrderStatus.PENDING;
            case OrderStatusJpa.PAID:
                return OrderStatus.PAID;
            case OrderStatusJpa.CANCELLED:
                return OrderStatus.CANCELLED;
            default:
                return null;
        }
    }

    public PaginatedEntity<Order> toPaginatedDomain(Page<OrderJpaEntity> page) {
        return toPaginatedDomain(page, null);
    }

    public PaginatedEntity<Order> toPaginatedDomain(Page<OrderJpaEntity> page, SortEntity fallbackSort) {
        SortEntity sortEntity;
        if (page.getSort() != null && page.getSort().isSorted() && !page.getSort().isEmpty()) {
            var order = page.getSort().toList().getFirst();
            sortEntity = new SortEntity(order.getProperty(), order.getDirection().name());
        } else if (fallbackSort != null) {
            sortEntity = fallbackSort;
        } else {
            sortEntity = new SortEntity("id", "ASC");
        }

        return new PaginatedEntity<Order>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent().stream().map(this::toDomain).collect(Collectors.toList()),
                page.isFirst(),
                page.isLast(),
                page.isEmpty(),
                sortEntity);
    }
}
