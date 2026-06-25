package cm.bognestanley.shop_backend.domain.product.repository;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.product.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);

    PaginatedEntity<Product> findAll(PaginationAttribute paginationAttribute, Boolean isActive);

    PaginatedEntity<Product> search(String name, Money minPrice, Money maxPrice, Boolean inStock, Boolean isActive,
            PaginationAttribute paginationAttribute);

    Product save(Product product);

    void delete(Product product);
}
