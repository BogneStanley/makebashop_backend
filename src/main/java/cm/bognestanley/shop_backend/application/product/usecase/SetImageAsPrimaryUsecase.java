package cm.bognestanley.shop_backend.application.product.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class SetImageAsPrimaryUsecase {
    private final ProductRepository productRepository;

    public SetImageAsPrimaryUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, Long imageId) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND, "Product with ID " + productId + " not found"));
        
        existingProduct.setImageAsPrimary(imageId);

        return productRepository.save(existingProduct);
    }
}
