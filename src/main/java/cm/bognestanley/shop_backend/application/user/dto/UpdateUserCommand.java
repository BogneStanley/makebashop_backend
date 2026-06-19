package cm.bognestanley.shop_backend.application.user.dto;

import cm.bognestanley.shop_backend.domain.user.entity.UserRole;

public record UpdateUserCommand(
        Long id,
        String email,
        String password,
        String firstName,
        String lastName,
        String avatar,
        UserRole role) {
}
