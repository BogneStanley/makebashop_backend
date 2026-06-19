package cm.bognestanley.shop_backend.presentation.facade;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cm.bognestanley.shop_backend.application.product.dto.CreateProductCommand;
import cm.bognestanley.shop_backend.application.product.dto.CreateProductVariantCommand;
import cm.bognestanley.shop_backend.application.product.dto.UpdateImagePositionCommand;
import cm.bognestanley.shop_backend.application.product.dto.UpdateProductCommand;
import cm.bognestanley.shop_backend.application.product.usecase.*;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.infrastructure.exception.StorageException;
import cm.bognestanley.shop_backend.presentation.dto.request.product.CreateProductRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.ProductVariantRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.UpdateImagePositionRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.UpdateProductRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductResponse;
import cm.bognestanley.shop_backend.presentation.mapper.FileMapper;
import cm.bognestanley.shop_backend.presentation.mapper.PresProductMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final BulkAddImageUsecase bulkAddImageUsecase;
    private final BulkAddVariantUsecase bulkAddVariantUsecase;
    private final BulkDeleteImageUsecase bulkDeleteImageUsecase;
    private final BulkDeleteVarianteUsecase bulkDeleteVarianteUsecase;
    private final BulkUpdateImagePositionUsecase bulkUpdateImagePosition;
    private final CreateProductUsecase createProductUsecase;
    private final DeleteProductUsecase deleteProductUsecase;
    private final GetAllProductsUsecase getAllProductsUsecase;
    private final GetProductUsecase getProductUsecase;
    private final SearchProductsUsecase searchProductsUsecase;
    private final SetImageAsPrimaryUsecase setImageAsPrimaryUsecase;
    private final UpdateProductUsecase updateProductUsecase;
    private final UpdateVariantUsecase updateVariantUsecase;

    private final PresProductMapper productMapper;
    private final FileMapper fileMapper;

    public PaginatedEntity<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortOrder) {
        PaginationAttribute paginationAttribute = new PaginationAttribute(page, size,
                new SortEntity(sortBy, sortOrder));
        return getAllProductsUsecase.execute(paginationAttribute).map(productMapper::toResponse);
    }

    public ProductResponse getProduct(Long id) {
        return productMapper.toResponse(getProductUsecase.execute(id));
    }

    public ProductResponse createProduct(CreateProductRequest request, List<MultipartFile> images) {

        if (images == null) {
            return productMapper
                    .toResponse(createProductUsecase.execute(productMapper.toCreateProductCommand(request, List.of())));
        }

        CreateProductCommand command = productMapper.toCreateProductCommand(request, images.stream().map(image -> {
            try {
                return fileMapper.toFileContent(image);
            } catch (IOException e) {
                throw new StorageException("Failed to store file", e);
            }
        }).toList());

        return productMapper.toResponse(createProductUsecase.execute(command));
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        UpdateProductCommand command = productMapper.toUpdateProductCommand(id, request);
        return productMapper.toResponse(updateProductUsecase.execute(command));
    }

    public void deleteProduct(Long id) {
        deleteProductUsecase.execute(id);
    }

    public PaginatedEntity<ProductResponse> searchProducts(String keyword, BigDecimal minPrice, BigDecimal maxPrice,
            Boolean inStock, int page, int size, String sortBy, String sortOrder) {
        PaginationAttribute paginationAttribute = new PaginationAttribute(page, size,
                new SortEntity(sortBy, sortOrder));
        PaginatedEntity<Product> products = searchProductsUsecase.execute(keyword, minPrice, maxPrice, null, inStock,
                paginationAttribute);

        return products.map(productMapper::toResponse);
    }

    public ProductResponse addImagesToProduct(Long productId, List<MultipartFile> files) {
        return productMapper.toResponse(bulkAddImageUsecase.execute(productId, files.stream().map(file -> {
            try {
                return fileMapper.toFileContent(file);
            } catch (IOException e) {
                throw new StorageException("Failed to store file", e);
            }
        }).toList()));
    }

    public ProductResponse addVariantsToProduct(Long productId, List<ProductVariantRequest> variants) {
        List<CreateProductVariantCommand> commandVariants = variants.stream()
                .map(productMapper::toCreateProductVariantCommand).toList();
        return productMapper.toResponse(bulkAddVariantUsecase.execute(productId, commandVariants));
    }

    public ProductResponse deleteImagesFromProduct(Long productId, List<Long> imageIds) {
        return productMapper.toResponse(bulkDeleteImageUsecase.execute(productId, imageIds));
    }

    public ProductResponse deleteVariantsFromProduct(Long productId, List<Long> variantIds) {
        return productMapper.toResponse(bulkDeleteVarianteUsecase.execute(productId, variantIds));
    }

    public ProductResponse updateImagesPosition(Long productId, List<UpdateImagePositionRequest> imagesPosition) {
        List<UpdateImagePositionCommand> commands = imagesPosition.stream()
                .map(productMapper::toUpdateImagePositionCommand).toList();
        return productMapper.toResponse(bulkUpdateImagePosition.execute(productId, commands));
    }

    public ProductResponse updateVariant(Long productId, Long variantId, ProductVariantRequest request) {
        return productMapper.toResponse(
                updateVariantUsecase.execute(productId,
                        productMapper.toUpdateProductVariantCommand(variantId, request)));
    }

    public ProductResponse setProductImageAsPrimary(Long productId, Long imageId) {
        return productMapper.toResponse(setImageAsPrimaryUsecase.execute(productId, imageId));
    }

}
