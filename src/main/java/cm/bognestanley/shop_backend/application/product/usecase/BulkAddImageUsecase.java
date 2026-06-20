package cm.bognestanley.shop_backend.application.product.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.common.port.FileStoragePort;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductImage;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class BulkAddImageUsecase {

    private final ProductRepository productRepository;
    private final FileStoragePort fileStoragePort;

    public BulkAddImageUsecase(ProductRepository productRepository, FileStoragePort fileStoragePort) {
        this.productRepository = productRepository;
        this.fileStoragePort = fileStoragePort;
    }

    @Transactional
    public Product execute(Long productId, List<FileContent> images) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found with ID: " + productId));

        for (FileContent image : images) {
            var storedFile = fileStoragePort.uploadFile(image);
            existingProduct.addImage(
                    new ProductImage(
                            null,
                            storedFile.fileUrl(),
                            storedFile.storageKey(),
                            storedFile.contentType(),
                            storedFile.fileName(),
                            false,
                            0));
        }

        return productRepository.save(existingProduct);
    }
}
