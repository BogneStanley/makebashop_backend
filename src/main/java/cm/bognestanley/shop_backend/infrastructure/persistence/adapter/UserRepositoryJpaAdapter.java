package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.user.criteria.SearchUserCriteria;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.user.UserJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.UserMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.UserJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.specidication.UserSpecification;

@Repository
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryJpaAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public PaginatedEntity<User> findAll(PaginationAttribute attribute) {
        Pageable pageable = PageRequest.of(
                attribute.pageNumber(),
                attribute.pageSize(),
                Sort.by(Direction.fromString(attribute.sort().direction()), attribute.sort().property()));
        Page<UserJpaEntity> page = userJpaRepository.findAll(pageable);
        return userMapper.toPaginatedDomain(page);
    }

    @Override
    public PaginatedEntity<User> search(String query, String role, PaginationAttribute attribute) {
        Pageable pageable = PageRequest.of(
                attribute.pageNumber(),
                attribute.pageSize(),
                Sort.by(Direction.fromString(attribute.sort().direction()), attribute.sort().property()));
        
        SearchUserCriteria criteria = new SearchUserCriteria(query, role);

        Specification<UserJpaEntity> spec = UserSpecification.toSpecification(criteria);

        Page<UserJpaEntity> page = userJpaRepository.findAll(spec, pageable);
        return userMapper.toPaginatedDomain(page);
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(userMapper.toJpa(user));
    }

    @Override
    public User save(User user) {
        return userMapper.toDomain(userJpaRepository.save(userMapper.toJpa(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
