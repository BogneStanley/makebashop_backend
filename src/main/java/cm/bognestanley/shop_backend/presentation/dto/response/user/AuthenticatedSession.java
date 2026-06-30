package cm.bognestanley.shop_backend.presentation.dto.response.user;

public record AuthenticatedSession(
        UserResponse user,
        String accessToken) {

    public AuthResponse toAuthResponse(boolean cookieMode) {
        return new AuthResponse(user, cookieMode ? null : accessToken);
    }
}
