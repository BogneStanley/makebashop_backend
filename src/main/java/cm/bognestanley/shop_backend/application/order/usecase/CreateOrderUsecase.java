package cm.bognestanley.shop_backend.application.order.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.cart.port.CartResolver;
import cm.bognestanley.shop_backend.application.order.dto.CreateOrderCommand;
import cm.bognestanley.shop_backend.domain.cart.entity.Cart;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.entity.OrderLineItem;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import cm.bognestanley.shop_backend.domain.order.valueObject.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final CartResolver cartResolver;

    public CreateOrderUsecase(OrderRepository orderRepository,
            CartResolver cartResolver) {
        this.orderRepository = orderRepository;
        this.cartResolver = cartResolver;
    }

    public Order execute(CreateOrderCommand command) {

        Cart cart = cartResolver.resolveCart();

        List<OrderLineItem> orderLineItems = cart.getCartItems().stream()
                .map(cartLineItem -> OrderLineItem.create(
                        cartLineItem.getProduct(),
                        cartLineItem.getProductVariant(),
                        cartLineItem.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));

        Order order = Order.create(
                new Customer(
                        command.customer().firstName(),
                        command.customer().lastName(),
                        command.customer().email(),
                        command.customer().phoneNumber()),
                command.note(),
                orderLineItems);


        return orderRepository.save(order);

    }
}
