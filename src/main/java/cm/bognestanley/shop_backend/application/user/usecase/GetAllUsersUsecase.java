package cm.bognestanley.shop_backend.application.user.usecase;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllUsersUsecase {

    private final UserRepository userRepository;

    public GetAllUsersUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PaginatedEntity<User> execute(PaginationAttribute attribute) {
        return userRepository.findAll(attribute);
    }
}
