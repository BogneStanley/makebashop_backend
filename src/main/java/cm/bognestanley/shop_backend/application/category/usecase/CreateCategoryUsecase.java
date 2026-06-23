package cm.bognestanley.shop_backend.application.category.usecase;

import cm.bognestanley.shop_backend.application.category.dto.CategoryCommand;
import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.domain.category.repository.CategoryRepository;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUsecase {
    private final CategoryRepository categoryRepository;

    public CreateCategoryUsecase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category execute(CategoryCommand command){

        if (categoryRepository.existByName(command.name())){
            throw new ApplicationException(ErrorCode.CATEGORY_ALREADY_EXIST_WITH_NAME);
        }

        Category category = Category.create(command.name(), command.description());
        return categoryRepository.save(category);
    }
}
