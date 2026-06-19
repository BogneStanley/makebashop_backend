package cm.bognestanley.shop_backend.presentation.dto.response.product;

import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    String description,
    List<ProductImageResponse> images,
    List<ProductVariantResponse> productVariants
) {

    public ProductResponse copyWith(List<ProductVariantResponse> variants) {
        return new ProductResponse(
            this.id,
            this.name,
            this.description,
            this.images,
            variants
        );
    }
    
}
