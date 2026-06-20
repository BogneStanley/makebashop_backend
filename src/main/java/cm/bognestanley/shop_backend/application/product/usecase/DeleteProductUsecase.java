package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
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
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "Product ID cannot be null");
        }
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND, "Product with ID " + id + " not found"));
        productRepository.delete(product);
    }
}
