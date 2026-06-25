package cm.bognestanley.shop_backend.presentation.dto.response.product;

import cm.bognestanley.shop_backend.presentation.dto.response.category.CategoryResponse;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        boolean isActive,
        List<CategoryResponse> categories,
        List<ProductImageResponse> images,
        List<ProductVariantResponse> productVariants
) {

    public ProductResponse copyWith(List<ProductVariantResponse> variants) {
        return new ProductResponse(
                this.id,
                this.name,
                this.description,
                this.isActive,
                this.categories,
                this.images,
                variants

        );
    }

}
