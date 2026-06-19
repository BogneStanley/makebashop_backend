package cm.bognestanley.shop_backend.application.user.dto;

public record LoginCommand(
        String email,
        String password) {
}
