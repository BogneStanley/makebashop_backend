package cm.bognestanley.shop_backend.application.category.usecase;

import cm.bognestanley.shop_backend.application.category.dto.CategoryCommand;
import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.domain.category.repository.CategoryRepository;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class UpdateCategoryUsecase {
    private final CategoryRepository categoryRepository;

    public UpdateCategoryUsecase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category execute(Long id, CategoryCommand command){
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!command.name().equalsIgnoreCase(existingCategory.getName()) && categoryRepository.existByName(command.name())){
            throw new ApplicationException(ErrorCode.CATEGORY_ALREADY_EXIST_WITH_NAME);
        }

        existingCategory.update(command.name(), command.description());
        return categoryRepository.save(existingCategory);
    }
}
