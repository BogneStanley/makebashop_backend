package cm.bognestanley.shop_backend.application.order.dto;

public record CustomerCommand(String firstName, String lastName, String email, String phoneNumber) {
}
