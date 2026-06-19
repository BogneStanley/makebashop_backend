package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllProductsUsecase {

    private final ProductRepository productRepository;

    public GetAllProductsUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PaginatedEntity<Product> execute(PaginationAttribute paginationAttribute) {
        if (paginationAttribute == null) {
            throw new IllegalArgumentException("Pagination attributes cannot be null");
        }
        return productRepository.findAll(paginationAttribute);
    }
}
