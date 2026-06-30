package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.domain.category.repository.CategoryRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.category.CategoryJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.CategoryMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.CategoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryJpaAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryMapper categoryMapper;

    public CategoryRepositoryJpaAdapter(CategoryJpaRepository categoryJpaRepository, CategoryMapper categoryMapper) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category save(Category category) {

        CategoryJpaEntity categoryJpaEntity = categoryMapper.toJpaEntity(category);
        CategoryJpaEntity savedCategory = categoryJpaRepository.save(categoryJpaEntity);

        return categoryMapper.toDomain(savedCategory) ;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id).map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream().map(categoryMapper::toDomain).toList();
    }

    @Override
    public List<Category> findByIds(List<Long> categoryIds) {
        return categoryJpaRepository.findAllById(categoryIds).stream().map(categoryMapper::toDomain).toList();
    }

    @Override
    public boolean existByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    @Override
    public void delete(Category category) {
        categoryJpaRepository.delete(categoryMapper.toJpaEntity(category));
    }
}
