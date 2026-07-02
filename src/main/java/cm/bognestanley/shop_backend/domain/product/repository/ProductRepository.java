package cm.bognestanley.shop_backend.domain.product.repository;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);

    List<Product> findAllByIds(List<Long> ids);

    PaginatedEntity<Product> findAll(PaginationAttribute paginationAttribute, Boolean isActive);

    PaginatedEntity<Product> search(String name, Money minPrice, Money maxPrice, Boolean inStock, Boolean isActive,
            List<Long> categoryIds, PaginationAttribute paginationAttribute);

    Product save(Product product);

    void delete(Product product);

    List<Product> findNewest(int limit, boolean activeOnly);

    List<Product> findMostPopular(int limit, boolean activeOnly);
}
