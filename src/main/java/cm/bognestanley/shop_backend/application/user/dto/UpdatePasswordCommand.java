package cm.bognestanley.shop_backend.application.user.dto;

public record UpdatePasswordCommand(
        Long userId,
        String currentPassword,
        String newPassword) {
}
