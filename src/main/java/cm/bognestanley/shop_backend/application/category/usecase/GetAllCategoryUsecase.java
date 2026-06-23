package cm.bognestanley.shop_backend.application.category.usecase;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.category.entity.Category;
import cm.bognestanley.shop_backend.domain.category.repository.CategoryRepository;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllCategoryUsecase {
    private final CategoryRepository categoryRepository;

    public GetAllCategoryUsecase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> execute(){
        return categoryRepository
                .findAll();

    }
}
