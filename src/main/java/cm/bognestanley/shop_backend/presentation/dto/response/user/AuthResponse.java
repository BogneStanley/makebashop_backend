package cm.bognestanley.shop_backend.presentation.dto.response.user;

import cm.bognestanley.shop_backend.domain.user.entity.UserRole;

public record AuthResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String avatar,
        UserRole role,
        String token) {
}
