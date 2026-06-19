package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.exception.ProductNotFoundException;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class GetProductUsecase {

    private final ProductRepository productRepository;

    public GetProductUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }
}
