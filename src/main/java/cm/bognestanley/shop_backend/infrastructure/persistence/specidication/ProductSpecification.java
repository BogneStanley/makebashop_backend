package cm.bognestanley.shop_backend.infrastructure.persistence.specidication;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import cm.bognestanley.shop_backend.domain.product.criteria.ProductSearchCriteria;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.category.CategoryJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class ProductSpecification {

    public static Specification<ProductJpaEntity> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            return builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<ProductJpaEntity> hasPriceRange(BigDecimal minPrice, BigDecimal maxPrice, boolean isSortByPrice) {
        return (root, query, builder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }

            applyDistinctIfNeeded(query, isSortByPrice);

            Join<ProductJpaEntity, ProductVariantJpaEntity> variantJoin = root.join("variants", JoinType.LEFT);

            if (minPrice != null && maxPrice != null) {
                return builder.between(variantJoin.get("price"), minPrice, maxPrice);
            }

            if (minPrice != null) {
                return builder.greaterThanOrEqualTo(variantJoin.get("price"), minPrice);
            }

            return builder.lessThanOrEqualTo(variantJoin.get("price"), maxPrice);
        };
    }

    public static Specification<ProductJpaEntity> hasActiveStatus(Boolean isActive) {
        return (root, query, builder) -> {
            if (isActive == null) {
                return null;
            }
            return builder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<ProductJpaEntity> hasCategoryIds(List<Long> categoryIds, boolean isSortByPrice) {
        return (root, query, builder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return null;
            }

            applyDistinctIfNeeded(query, isSortByPrice);

            Join<ProductJpaEntity, CategoryJpaEntity> categoryJoin = root.join("categories", JoinType.LEFT);
            return categoryJoin.get("id").in(categoryIds);
        };
    }

    public static Specification<ProductJpaEntity> hasStock(Boolean inStock, boolean isSortByPrice) {
        return (root, query, builder) -> {
            if (inStock == null) {
                return null;
            }

            applyDistinctIfNeeded(query, isSortByPrice);

            Join<ProductJpaEntity, ProductVariantJpaEntity> variantJoin = root.join("variants", JoinType.LEFT);

            if (inStock) {
                return builder.greaterThan(variantJoin.get("stockQuantity"), 0);
            }

            return builder.lessThanOrEqualTo(variantJoin.get("stockQuantity"), 0);
        };
    }

    public static Specification<ProductJpaEntity> distinct(boolean isSortByPrice) {
        return (root, query, builder) -> {
            applyDistinctIfNeeded(query, isSortByPrice);
            return null;
        };
    }

    public static Specification<ProductJpaEntity> sortByPrice(String sortOrder) {
        return (root, query, builder) -> {
            if (isCountQuery(query)) {
                return null;
            }

            Join<ProductJpaEntity, ProductVariantJpaEntity> variantJoin = root.join("variants", JoinType.LEFT);
            query.groupBy(root.get("id"));

            boolean isAsc = !"desc".equalsIgnoreCase(sortOrder);
            if (isAsc) {
                query.orderBy(builder.asc(builder.min(variantJoin.get("price"))));
            } else {
                query.orderBy(builder.desc(builder.max(variantJoin.get("price"))));
            }

            return null;
        };
    }

    public static Specification<ProductJpaEntity> toSpecification(ProductSearchCriteria criteria, String sortBy, String sortOrder) {
        boolean isSortByPrice = "price".equalsIgnoreCase(sortBy);

        Specification<ProductJpaEntity> spec = Specification.where(distinct(isSortByPrice))
                .and(hasKeyword(criteria.name()))
                .and(hasPriceRange(criteria.minPrice(), criteria.maxPrice(), isSortByPrice))
                .and(hasStock(criteria.inStock(), isSortByPrice))
                .and(hasActiveStatus(criteria.isActive()))
                .and(hasCategoryIds(criteria.categoryIds(), isSortByPrice));

        if (isSortByPrice) {
            spec = spec.and(sortByPrice(sortOrder));
        }

        return spec;
    }

    private static void applyDistinctIfNeeded(CommonAbstractCriteria query, boolean isSortByPrice) {
        if (query == null) {
            return;
        }
        if (isCountQuery(query) || !isSortByPrice) {
            if (query instanceof CriteriaQuery) {
                ((CriteriaQuery<?>) query).distinct(true);
            }
        }
    }

    private static boolean isCountQuery(CommonAbstractCriteria query) {
        if (query instanceof CriteriaQuery) {
            Class<?> resultType = ((CriteriaQuery<?>) query).getResultType();
            return resultType == Long.class || resultType == long.class;
        }
        return false;
    }
}
