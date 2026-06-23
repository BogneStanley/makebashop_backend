package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.category.CategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryJpaEntity toJpaEntity(Category category){
        return CategoryJpaEntity
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public Category toDomain(CategoryJpaEntity categoryJpaEntity){
        return new Category(categoryJpaEntity.getId(), categoryJpaEntity.getName(), categoryJpaEntity.getDescription());
    }
}
