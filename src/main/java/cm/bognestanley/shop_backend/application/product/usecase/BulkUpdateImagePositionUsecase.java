package cm.bognestanley.shop_backend.application.product.usecase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.product.dto.UpdateImagePositionCommand;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.exception.ProductNotFoundException;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

@Service
public class BulkUpdateImagePositionUsecase {

    private final ProductRepository productRepository;

    public BulkUpdateImagePositionUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, List<UpdateImagePositionCommand> commands) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        Map<Long, Integer> commandsMap = commands.stream()
                .collect(
                        Collectors.toMap(UpdateImagePositionCommand::imageId, UpdateImagePositionCommand::newPosition));

        existingProduct.updateImagesPosition(commandsMap);

        return productRepository.save(existingProduct);
    }
}
