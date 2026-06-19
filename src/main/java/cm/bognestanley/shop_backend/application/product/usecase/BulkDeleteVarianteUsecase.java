package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BulkDeleteVarianteUsecase {

    private final ProductRepository productRepository;

    public BulkDeleteVarianteUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, List<Long> variantIds) {
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id " + productId));

        product.removeVariants(variantIds);
        
        return productRepository.save(product);
    }
}
