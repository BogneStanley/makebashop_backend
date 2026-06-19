package cm.bognestanley.shop_backend.infrastructure.persistence.specidication;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import cm.bognestanley.shop_backend.domain.product.criteria.ProductSearchCriteria;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import jakarta.persistence.criteria.Join;

public class ProductSpecification {
    public static Specification<ProductJpaEntity> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            return builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<ProductJpaEntity> hasPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, builder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }

            query.distinct(true);

            Join<ProductJpaEntity, ProductVariantJpaEntity> variantJoin = root.join("variants");

            if(minPrice != null && maxPrice != null) {
                return builder.between(variantJoin.get("price"), minPrice, maxPrice);
            }

            if(minPrice != null) {
                return builder.greaterThanOrEqualTo(variantJoin.get("price"), minPrice);
            }

            return builder.lessThanOrEqualTo(variantJoin.get("price"), maxPrice);

        };
    }

    public static Specification<ProductJpaEntity> hasStock(Boolean inStock) {
        return (root, query, builder) -> {
            if (inStock == null) {
                return null;
            }

            query.distinct(true);

            Join<ProductJpaEntity, ProductVariantJpaEntity> variantJoin = root.join("variants");

            if (inStock) {
                return builder.greaterThan(variantJoin.get("stockQuantity"), 0);
            }

            return builder.lessThanOrEqualTo(variantJoin.get("stockQuantity"), 0);
        };
    }

    public static Specification<ProductJpaEntity> toSpecification(ProductSearchCriteria criteria) {
        return Specification.where(hasKeyword(criteria.name()))
                .and(hasPriceRange(criteria.minPrice(), criteria.maxPrice()))
                .and(hasStock(criteria.inStock()));
    }
}
