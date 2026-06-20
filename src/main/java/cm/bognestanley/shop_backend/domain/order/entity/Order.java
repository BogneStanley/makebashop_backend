package cm.bognestanley.shop_backend.domain.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.order.valueObject.Customer;
import cm.bognestanley.shop_backend.domain.order.valueObject.OrderStatus;
import cm.bognestanley.shop_backend.domain.product.entity.Product;

public class Order {
    private Long id;
    private String orderNumber;
    private Customer customer;
    private List<OrderLineItem> orderLineItems;
    private OrderStatus status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(Long id, String orderNumber, Customer customer, List<OrderLineItem> orderLineItems,
            OrderStatus status, String note, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (orderNumber == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Order number cannot be null");
        }
        if (status == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Order status cannot be null");
        }

        this.id = id;
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.status = status;
        this.note = note;
        this.orderLineItems = orderLineItems != null ? orderLineItems : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(Customer customer, String note) {
        return new Order(null, UUID.randomUUID().toString(), customer, new ArrayList<>(),
                OrderStatus.PENDING, note, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Order create(Customer customer, String note, List<OrderLineItem> orderLineItems) {
        return new Order(null, UUID.randomUUID().toString(), customer, orderLineItems,
                OrderStatus.PENDING, note, LocalDateTime.now(), LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public Money totalAmount() {

        if (orderLineItems == null || orderLineItems.isEmpty()) {
            return Money.ZERO;
        }

        Money totalAmount = orderLineItems.stream()
                .map(OrderLineItem::subtotal)
                .reduce(Money::add)
                .orElse(null);
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Update methods
    public void updateStatus(OrderStatus status) {
        if (status == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Order status cannot be null");
        }
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // Line item management
    public void addLineItem(OrderLineItem lineItem) {
        if (lineItem == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Order line item cannot be null");
        }
        this.orderLineItems.add(lineItem);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeLineItem(OrderLineItem lineItem) {
        if (lineItem == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Order line item cannot be null");
        }
        boolean removed = this.orderLineItems.remove(lineItem);
        if (removed) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void updateLineItemQuantity(OrderLineItem lineItem, int quantity) {
        if (lineItem == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Order line item cannot be null");
        }
        if (quantity <= 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Quantity must be greater than 0");
        }
        if (!this.orderLineItems.contains(lineItem)) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Line item not found in order");
        }

        lineItem.updateQuantity(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsPaid() {
        if (this.status == OrderStatus.PAID || this.status == OrderStatus.CANCELLED) {
            throw new DomainErrorException(ErrorCode.INVALID_ORDER_SWITCH_STATUS);
        }
        updateProductVariantStock();
        this.status = OrderStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public Set<Product> getProducts() {
        Set<Product> products = new HashSet<>();
        for (OrderLineItem item : orderLineItems) {
            if (item.getProduct() != null && !products.stream().anyMatch(product -> product.getId()
                    .equals(item.getProduct().getId()))) {
                products.add(item.getProduct());
            }
        }
        return products;
    }

    public void markAsCancelled() {
        if (this.status == OrderStatus.PAID || this.status == OrderStatus.CANCELLED) {
            throw new DomainErrorException(ErrorCode.INVALID_ORDER_SWITCH_STATUS);
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    private void updateProductVariantStock() {
        this.orderLineItems = this.orderLineItems.stream().map(item -> {
            if (!item.getProductVariant().hasSufficientStock(item.getQuantity())) {
                throw new DomainErrorException(ErrorCode.OUT_OF_STOCK);
            }

            item.getProduct().getProductVariants().stream()
                    .filter(variant -> variant.getId().equals(item.getProductVariant().getId())).findFirst().get()
                    .decreaseStock(item.getQuantity());

            item.getProductVariant().decreaseStock(item.getQuantity());

            return item;
        }).collect(Collectors.toCollection(ArrayList::new));

    }
}
