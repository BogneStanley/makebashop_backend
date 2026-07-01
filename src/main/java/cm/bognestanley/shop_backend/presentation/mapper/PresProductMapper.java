package cm.bognestanley.shop_backend.presentation.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cm.bognestanley.shop_backend.application.common.dto.FileContent;
import cm.bognestanley.shop_backend.infrastructure.config.UploadProperties;
import cm.bognestanley.shop_backend.application.product.dto.CreateProductCommand;
import cm.bognestanley.shop_backend.application.product.dto.CreateProductVariantCommand;
import cm.bognestanley.shop_backend.application.product.dto.UpdateImagePositionCommand;
import cm.bognestanley.shop_backend.application.product.dto.UpdateProductCommand;
import cm.bognestanley.shop_backend.application.product.dto.UpdateProductVariantCommand;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductImage;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;
import cm.bognestanley.shop_backend.presentation.dto.request.product.CreateProductRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.ProductVariantRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.UpdateImagePositionRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.UpdateProductRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.MoneyResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductImageResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductVariantResponse;

@Component
public class PresProductMapper {

    @Value("${app.currency-code:FCFA}")
    private String currency;

    private final PresCategoryMapper categoryMapper;
    private final UploadProperties uploadProperties;

    public PresProductMapper(PresCategoryMapper categoryMapper, UploadProperties uploadProperties) {
        this.categoryMapper = categoryMapper;
        this.uploadProperties = uploadProperties;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        List<ProductVariantResponse> variantResponses = product.getProductVariants()
                .stream()
                .map(this::toVariantResponse)
                .toList();

        List<ProductImageResponse> imageResponses = product.getImages()
                .stream()
                .map(this::toImageResponse)
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.isActive(),
                product.getCategories().stream().map(categoryMapper::toCategoryResponse).toList(),
                imageResponses,
                variantResponses);
    }

    public ProductVariantResponse toVariantResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }
        return new ProductVariantResponse(
                variant.getId(),
                variant.getSku(),
                new MoneyResponse(variant.getPrice().amount(), variant.getPrice().currency()),
                variant.getStockQuantity(),
                variant.getColor(),
                variant.getSize());
    }

    public ProductImageResponse toImageResponse(ProductImage image) {
        if (image == null) {
            return null;
        }
        return new ProductImageResponse(
                image.getId(),
                uploadProperties.resolvePublicUrl(image.getUrl()),
                image.getFileName(),
                image.getPosition(),
                image.isPrimary());
    }

    public CreateProductCommand toCreateProductCommand(CreateProductRequest request, List<FileContent> images) {
        if (request == null) {
            return null;
        }
        return new CreateProductCommand(
                request.name(),
                request.description(),
                request.productVariants().stream().map(this::toCreateProductVariantCommand).toList(),
                request.categoryIds(),
                images);
    }

    public CreateProductVariantCommand toCreateProductVariantCommand(ProductVariantRequest request) {
        if (request == null) {
            return null;
        }
        return new CreateProductVariantCommand(
                request.sku(),
                request.price(),
                currency,
                request.stockQuantity(),
                request.size(),
                request.color());
    }

    public UpdateProductCommand toUpdateProductCommand(Long id, UpdateProductRequest request) {
        return new UpdateProductCommand(id, request.name(), request.description(), request.isActive(), request.categoryIds());
    }

    public UpdateImagePositionCommand toUpdateImagePositionCommand(UpdateImagePositionRequest request) {
        return new UpdateImagePositionCommand(request.imageId(), request.position());
    }

    public UpdateProductVariantCommand toUpdateProductVariantCommand(Long variantId, ProductVariantRequest request) {
        return new UpdateProductVariantCommand(variantId, request.sku(), request.price(), currency, request.stockQuantity(), request.size(), request.color());
    }

}
