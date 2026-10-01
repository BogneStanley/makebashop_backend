package cm.bognestanley.shop_backend.application.order.usecase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.application.order.dto.CreateOrderCommand;
import cm.bognestanley.shop_backend.application.order.port.InventoryReservationPort;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.entity.OrderLineItem;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import cm.bognestanley.shop_backend.domain.order.valueObject.Customer;
import cm.bognestanley.shop_backend.infrastructure.config.OrderReservationProperties;


@Service
public class CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final CartResolver cartResolver;
    private final InventoryReservationPort inventoryReservation;
    private final cm.bognestanley.shop_backend.domain.cart.repository.CartRepository cartRepository;
    private final OrderReservationProperties reservationProperties;

    public CreateOrderUsecase(OrderRepository orderRepository,
            CartResolver cartResolver, InventoryReservationPort inventoryReservation,
            cm.bognestanley.shop_backend.domain.cart.repository.CartRepository cartRepository,
            OrderReservationProperties reservationProperties) {
        this.orderRepository = orderRepository;
        this.cartResolver = cartResolver;
        this.inventoryReservation = inventoryReservation;
        this.cartRepository = cartRepository;
        this.reservationProperties = reservationProperties;
    }

    @Transactional
    public Order execute(CreateOrderCommand command) {

        Cart cart = cartResolver.resolveCart();
        String fingerprint = fingerprint(command);

        var existingOrder = orderRepository.findByIdempotencyKey(command.idempotencyKey());
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            if (!cart.getId().equals(order.getCartId()) || !fingerprint.equals(order.getRequestFingerprint())) {
                throw new DomainErrorException(ErrorCode.IDEMPOTENCY_KEY_REUSED);
            }
            return order;
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new DomainErrorException(ErrorCode.CART_EMPTY);
        }

        var orderLineItems = inventoryReservation.reserve(cart.getCartItems());

        Order order = Order.create(
                new Customer(
                        command.customer().firstName(),
                        command.customer().lastName(),
                        command.customer().email(),
                        command.customer().phoneNumber()),
                command.note(), orderLineItems, cart.getId(), command.idempotencyKey(), fingerprint,
                LocalDateTime.now().plusMinutes(reservationProperties.getReservationDurationMinutes()));

        Order savedOrder = orderRepository.save(order);
        cart.clearCart();
        cartRepository.save(cart);
        return savedOrder;
    }

    private String fingerprint(CreateOrderCommand command) {
        String normalized = String.join("|",
                normalize(command.customer().firstName()), normalize(command.customer().lastName()),
                normalize(command.customer().email()), normalize(command.customer().phoneNumber()),
                normalize(command.note()));
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(normalized.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
