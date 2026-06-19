package cm.bognestanley.shop_backend.domain.order.valueObject;

public record Customer(
        String firstName,
        String lastName,
        String email,
        String phoneNumber) {
}
