package cm.bognestanley.shop_backend.application.category.usecase;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.domain.category.repository.CategoryRepository;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class DeleteCategoryUsecase {
    private final CategoryRepository categoryRepository;

    public DeleteCategoryUsecase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void execute(Long id){
         Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));

         categoryRepository.delete(category);

    }
}
