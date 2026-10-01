package cm.bognestanley.shop_backend.application.setup.dto;

public record InitialAdminSetupCommand(
        String email,
        String firstName,
        String lastName,
        String password) {
}
