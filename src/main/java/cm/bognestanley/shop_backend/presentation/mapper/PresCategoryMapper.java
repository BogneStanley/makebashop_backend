package cm.bognestanley.shop_backend.presentation.mapper;

import cm.bognestanley.shop_backend.application.category.dto.CategoryCommand;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.presentation.dto.request.category.CategoryRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.category.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class PresCategoryMapper {

    public CategoryCommand toCategoryCommand(CategoryRequest request){
        return new CategoryCommand(request.name(), request.description());
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
