package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.exception.ProductNotFoundException;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteProductUsecase {

    private final ProductRepository productRepository;

    public DeleteProductUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        productRepository.delete(product);
    }
}
