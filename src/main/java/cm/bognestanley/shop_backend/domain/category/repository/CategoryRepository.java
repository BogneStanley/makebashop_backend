package cm.bognestanley.shop_backend.domain.category.repository;

import cm.bognestanley.shop_backend.domain.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    List<Category> findByIds(List<Long> categoryIds);
    boolean existByName(String name);
    void delete(Category category);
}
