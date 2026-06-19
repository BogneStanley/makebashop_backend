package cm.bognestanley.shop_backend.presentation.dto.request.order;

public record CreateOrderRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String note) {
}
