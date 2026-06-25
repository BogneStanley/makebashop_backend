package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductImage;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductImageJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;

@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ProductJpaEntity toJpa(Product product) {
        ProductJpaEntity productJpaEntity = ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .isActive(product.isActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .variants(new HashSet<>())
                .images(new HashSet<>())
                .categories(product.getCategories().stream().map(categoryMapper::toJpaEntity).collect(Collectors.toSet()))
                .build();

        if (product.getProductVariants() != null) {
            for (ProductVariant variant : product.getProductVariants()) {
                ProductVariantJpaEntity variantJpaEntity = toProductVariantJpa(variant);
                variantJpaEntity.setProduct(productJpaEntity);
                productJpaEntity.getVariants().add(variantJpaEntity);
            }
        }
        if (product.getImages() != null) {
            for (ProductImage image : product.getImages()) {
                ProductImageJpaEntity imageJpaEntity = toProductImageJpa(image);
                imageJpaEntity.setProduct(productJpaEntity);
                productJpaEntity.getImages().add(imageJpaEntity);
            }
        }
        return productJpaEntity;
    }

    public Product toDomain(ProductJpaEntity save) {
        return new Product(
                save.getId(),
                save.getName(),
                save.getDescription(),
                save.isActive(),
                save.getVariants().stream().map(this::toProductVariantDomain).collect(Collectors.toList()),
                save.getImages().stream().map(this::toProductImageDomain).collect(Collectors.toList()),
                save.getCategories().stream().map(categoryMapper::toDomain).toList(),
                save.getCreatedAt(),
                save.getUpdatedAt());
    }

    public PaginatedEntity<Product> toPaginatedDomain(Page<ProductJpaEntity> page) {
        return new PaginatedEntity<Product>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent().stream().map(this::toDomain).collect(Collectors.toList()),
                page.isFirst(),
                page.isLast(),
                page.isEmpty(),
                new SortEntity(page.getSort().toList().getFirst().getProperty(),
                        page.getSort().toList().getFirst().getDirection().name()));
    }

    public ProductVariant toProductVariantDomain(ProductVariantJpaEntity variant) {
        return new ProductVariant(
                variant.getId(),
                variant.getSku(),
                new Money(variant.getPrice(), variant.getCurrencyCode()),
                variant.getStockQuantity(),
                variant.getSize(),
                variant.getColor(),
                variant.getCreatedAt(),
                variant.getUpdatedAt());
    }

    public ProductImage toProductImageDomain(ProductImageJpaEntity image) {
        return new ProductImage(
                image.getId(),
                image.getUrl(),
                image.getStorageKey(),
                image.getContentType(),
                image.getFileName(),
                image.isPrimary(),
                image.getPosition());
    }

    public ProductImageJpaEntity toProductImageJpa(ProductImage image) {
        return ProductImageJpaEntity.builder()
                .id(image.getId())
                .url(image.getUrl())
                .storageKey(image.getStorageKey())
                .contentType(image.getContentType())
                .fileName(image.getFileName())
                .isPrimary(image.isPrimary())
                .position(image.getPosition())
                .build();
    }

    public ProductVariantJpaEntity toProductVariantJpa(ProductVariant variant) {
        return ProductVariantJpaEntity.builder()
                .id(variant.getId())
                .sku(variant.getSku())
                .price(variant.getPrice().amount())
                .currencyCode(variant.getPrice().currency())
                .stockQuantity(variant.getStockQuantity())
                .size(variant.getSize())
                .color(variant.getColor())
                .createdAt(variant.getCreatedAt())
                .updatedAt(variant.getUpdatedAt())
                .build();
    }

}
