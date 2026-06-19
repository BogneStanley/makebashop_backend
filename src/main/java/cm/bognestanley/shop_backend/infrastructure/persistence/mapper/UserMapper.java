package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.user.UserJpaEntity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

@Component
public class UserMapper {

    public UserJpaEntity toJpa(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(user.getAvatar())
                .password(user.getPassword())
                .role(user.getRole())
                .isActivate(user.isActivate())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public User toDomain(UserJpaEntity jpaEntity) {
        return new User(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getFirstName(),
                jpaEntity.getLastName(),
                jpaEntity.getAvatar(),
                jpaEntity.getPassword(),
                jpaEntity.getRole(),
                jpaEntity.isActivate(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }

    public PaginatedEntity<User> toPaginatedDomain(Page<UserJpaEntity> page) {
        List<SortEntity> sortEntities = page.getSort().stream()
                .map(order -> new SortEntity(order.getProperty(), order.getDirection().name()))
                .toList();
        SortEntity sortEntity = sortEntities.isEmpty()
                ? new SortEntity("id", "ASC")
                : sortEntities.getFirst();

        return new PaginatedEntity<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent().stream().map(this::toDomain).collect(Collectors.toList()),
                page.isFirst(),
                page.isLast(),
                page.isEmpty(),
                sortEntity);
    }
}
