package cm.bognestanley.shop_backend.application.product.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.common.port.FileStoragePort;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductImage;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class BulkDeleteImageUsecase {

    private final ProductRepository productRepository;
    private final FileStoragePort fileStoragePort;

    public BulkDeleteImageUsecase(ProductRepository productRepository, FileStoragePort fileStoragePort) {
        this.productRepository = productRepository;
        this.fileStoragePort = fileStoragePort;
    }
    
    public Product execute(Long productId, List<Long> imageIds) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND, "Product with ID " + productId + " not found"));
        
        List<ProductImage> removedImages = existingProduct.removeImagesByIds(imageIds);
        for (ProductImage image : removedImages) {
            fileStoragePort.deleteFile(image.getStorageKey());
        }
        return productRepository.save(existingProduct);
    }
}
