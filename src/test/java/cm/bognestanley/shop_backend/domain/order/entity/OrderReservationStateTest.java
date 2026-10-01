package cm.bognestanley.shop_backend.domain.order.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.order.valueObject.Customer;
import cm.bognestanley.shop_backend.domain.order.valueObject.OrderStatus;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;

class OrderReservationStateTest {

    @Test
    void paymentOnlyChangesStateBecauseStockWasReservedAtCheckout() {
        ProductVariant variant = new ProductVariant(10L, "SKU-10", new Money(new BigDecimal("2500"), "FCFA"),
                3, null, null, LocalDateTime.now(), LocalDateTime.now());
        Product product = new Product(1L, "Produit", "Description", null, null, true,
                List.of(variant), List.of(), List.of(), LocalDateTime.now(), LocalDateTime.now());
        Order order = Order.create(new Customer("Ada", "Lovelace", null, "+237690000000"), null,
                List.of(OrderLineItem.create(product, variant, 1)), 4L, "key", "fingerprint",
                LocalDateTime.now().plusHours(1));

        order.markAsPaid();

        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(3, variant.getStockQuantity());
        assertThrows(DomainErrorException.class, order::markAsCancelled);
    }

    @Test
    void cancellationIsOnlyAllowedFromPending() {
        Order order = Order.create(new Customer("Ada", "Lovelace", null, "+237690000000"), null,
                List.of(), 4L, "key", "fingerprint", LocalDateTime.now().plusHours(1));

        order.markAsCancelled();

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertThrows(DomainErrorException.class, order::markAsPaid);
    }
}
