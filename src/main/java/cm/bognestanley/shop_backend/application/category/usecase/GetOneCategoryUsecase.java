package cm.bognestanley.shop_backend.application.category.usecase;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.domain.category.repository.CategoryRepository;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class GetOneCategoryUsecase {
    private final CategoryRepository categoryRepository;

    public GetOneCategoryUsecase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category execute(Long id){
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));

    }
}
