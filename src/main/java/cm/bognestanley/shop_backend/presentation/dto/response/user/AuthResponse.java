package cm.bognestanley.shop_backend.presentation.dto.response.user;

public record AuthResponse(
        UserResponse user,
        String token) {
}
