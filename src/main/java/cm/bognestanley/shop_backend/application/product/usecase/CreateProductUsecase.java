package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.application.common.dto.StoredFile;
import cm.bognestanley.shop_backend.application.common.port.FileStoragePort;
import cm.bognestanley.shop_backend.application.product.dto.CreateProductCommand;
import cm.bognestanley.shop_backend.application.product.dto.CreateProductVariantCommand;
import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductImage;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public class CreateProductUsecase {

    private final ProductRepository productRepository;
    private final FileStoragePort fileStoragePort;

    public CreateProductUsecase(ProductRepository productRepository, FileStoragePort fileStoragePort){
        this.productRepository = productRepository;
        this.fileStoragePort = fileStoragePort;
    }

    public Product execute(CreateProductCommand command){
        

        Product product = new Product(
            command.name(),
            command.description()
        );

        if (command.images() != null) {
            for (FileContent image : command.images()) {
                StoredFile storedFile = fileStoragePort.uploadFile(image);
                product.addImage(
                    new ProductImage(
                        null,
                        storedFile.fileUrl(),
                        storedFile.storageKey(),
                        storedFile.contentType(),
                        storedFile.fileName(),
                        false,
                        0
                    )
                );
            }
        }

        if (command.productVariants() != null) {
            for (CreateProductVariantCommand productVariantCommand : command.productVariants()) {
                ProductVariant productVariant = ProductVariant.create(
                    productVariantCommand.sku(),
                    new Money(
                        productVariantCommand.price(),
                        productVariantCommand.currencyCode()
                    ),
                    productVariantCommand.stockQuantity(),
                    productVariantCommand.size(),
                    productVariantCommand.color()
                );
                product.addVariant(productVariant);
            }
        }
        
        return productRepository.save(product);
    }
}
