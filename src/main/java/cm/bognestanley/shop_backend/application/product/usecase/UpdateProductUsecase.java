package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.application.product.dto.UpdateProductCommand;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.exception.ProductNotFoundException;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public class UpdateProductUsecase {

    private final ProductRepository productRepository;

    public UpdateProductUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(UpdateProductCommand command) {
        if (command == null || command.id() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Product existingProduct = productRepository.findById(command.id())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + command.id() + " not found"));

        


        existingProduct.update(
                command.name(),
                command.description(),
                null,
                null);


        return productRepository.save(existingProduct);
    }
}
