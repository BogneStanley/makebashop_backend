package cm.bognestanley.shop_backend.application.product.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.product.dto.UpdateProductVariantCommand;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class UpdateVariantUsecase {

    ProductRepository productRepository;

    public UpdateVariantUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, UpdateProductVariantCommand command) {
        var product = productRepository.findById(productId).orElseThrow();
        product.updateProductVariant(command.toDomainEntity());
        return productRepository.save(product);
    }
    
}
