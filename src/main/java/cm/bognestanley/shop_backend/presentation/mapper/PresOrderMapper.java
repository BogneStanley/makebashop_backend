package cm.bognestanley.shop_backend.presentation.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.entity.OrderLineItem;
import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.order.OrderItemResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.order.OrderResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductVariantResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PresOrderMapper {

    public final PresProductMapper productMapper;

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().toString(),
                order.getCustomer().firstName(),
                order.getCustomer().lastName(),
                order.getCustomer().email(),
                order.getCustomer().phoneNumber(),
                order.getNote(),
                new MoneyResponse(order.totalAmount().amount(), order.totalAmount().currency()),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getOrderLineItems().stream().map(this::toOrderItemResponse).toList());
    }

    public OrderItemResponse toOrderItemResponse(OrderLineItem orderLineItem) {
        return new OrderItemResponse(
                orderLineItem.getId(),
                productMapper.toResponse(orderLineItem.getProduct()).copyWith(new ArrayList<ProductVariantResponse>()),
                productMapper.toVariantResponse(orderLineItem.getProductVariant()),
                orderLineItem.getQuantity(),
                new MoneyResponse(orderLineItem.getPrice().amount(), orderLineItem.getPrice().currency()),
                new MoneyResponse(orderLineItem.subtotal().amount(), orderLineItem.subtotal().currency()));
    }
}
