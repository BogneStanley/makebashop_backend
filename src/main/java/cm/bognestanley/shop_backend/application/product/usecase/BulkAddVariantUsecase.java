package cm.bognestanley.shop_backend.application.product.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.product.dto.CreateProductVariantCommand;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.exception.ProductNotFoundException;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class BulkAddVariantUsecase {

    private final ProductRepository productRepository;

    public BulkAddVariantUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, List<CreateProductVariantCommand> variants) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));
        
        for (CreateProductVariantCommand variant : variants) {
            existingProduct.addVariant(variant.toDomainEntity());
        }

        return productRepository.save(existingProduct);
    }
}
