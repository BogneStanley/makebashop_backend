package cm.bognestanley.shop_backend.application.order.dto;

public record CreateOrderCommand(CustomerCommand customer, String note) {
}
