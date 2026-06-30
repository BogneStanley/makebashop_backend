package cm.bognestanley.shop_backend.presentation.facade;

import cm.bognestanley.shop_backend.application.category.dto.CategoryCommand;
import cm.bognestanley.shop_backend.application.category.usecase.*;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.presentation.dto.request.category.CategoryRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.category.CategoryResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryFacade {
    private final CreateCategoryUsecase createCategoryUsecase;
    private final DeleteCategoryUsecase deleteCategoryUsecase;
    private final GetAllCategoryUsecase getAllCategoryUsecase;
    private final GetOneCategoryUsecase getOneCategoryUsecase;
    private final UpdateCategoryUsecase updateCategoryUsecase;
    private final PresCategoryMapper categoryMapper;


    public CategoryResponse createCategory(CategoryRequest request){
        CategoryCommand categoryCommand = categoryMapper.toCategoryCommand(request);
        Category category = createCategoryUsecase.execute(categoryCommand);
        return categoryMapper.toCategoryResponse(category);
    }

    public void deleteCategory(Long id){
        deleteCategoryUsecase.execute(id);
    }

    public List<CategoryResponse> getCategories(){
        return getAllCategoryUsecase
                .execute()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategory(Long id){
        Category category = getOneCategoryUsecase.execute(id);
        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request){
        CategoryCommand command = categoryMapper.toCategoryCommand(request);
        Category category = updateCategoryUsecase.execute(id, command);
        return categoryMapper.toCategoryResponse(category);
    }
}
