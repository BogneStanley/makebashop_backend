package cm.bognestanley.shop_backend.presentation.dto.response.user;

import cm.bognestanley.shop_backend.domain.user.entity.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String avatar,
        UserRole role,
        boolean isActivate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
