package cm.bognestanley.shop_backend.application.product.usecase;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class SearchProductsUsecase {

    private final ProductRepository productRepository;

    public SearchProductsUsecase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PaginatedEntity<Product> execute(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String currencyCode,
            Boolean inStock,
            PaginationAttribute paginationAttribute) {

        if (paginationAttribute == null) {
            throw new IllegalArgumentException("Pagination attributes cannot be null");
        }

        Money minPriceVal = null;
        Money maxPriceVal = null;

        if (minPrice != null || maxPrice != null) {
            if (currencyCode == null || currencyCode.isBlank()) {
                throw new IllegalArgumentException("Currency code cannot be null or empty when searching by price range");
            }
            if (minPrice != null) {
                minPriceVal = Money.of(minPrice, currencyCode);
            }
            if (maxPrice != null) {
                maxPriceVal = Money.of(maxPrice, currencyCode);
            }
        }

        return productRepository.search(name, minPriceVal, maxPriceVal, inStock, paginationAttribute);
    }
}
