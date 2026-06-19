package cm.bognestanley.shop_backend.domain.user.repository;

import java.util.Optional;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.user.entity.User;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    PaginatedEntity<User> findAll(PaginationAttribute attribute);
    PaginatedEntity<User> search(String query, String role, PaginationAttribute attribute);
    void delete(User user);
    User save(User user);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
}
