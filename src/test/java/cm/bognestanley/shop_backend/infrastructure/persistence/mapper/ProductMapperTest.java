package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        CategoryMapper categoryMapper = new CategoryMapper();
        productMapper = new ProductMapper(categoryMapper);
    }

    @Test
    void toPaginatedDomain_WithUnsortedPage_ShouldUseFallbackSort() {
        Page<ProductJpaEntity> emptyUnsortedPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 10),
                0
        );

        SortEntity fallbackSort = new SortEntity("price", "ASC");
        PaginatedEntity<Product> result = productMapper.toPaginatedDomain(emptyUnsortedPage, fallbackSort);

        assertNotNull(result);
        assertEquals("price", result.sort().property());
        assertEquals("ASC", result.sort().direction());
    }

    @Test
    void toPaginatedDomain_WithSortedPage_ShouldUsePageSort() {
        Page<ProductJpaEntity> sortedPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")),
                0
        );

        SortEntity fallbackSort = new SortEntity("price", "ASC");
        PaginatedEntity<Product> result = productMapper.toPaginatedDomain(sortedPage, fallbackSort);

        assertNotNull(result);
        assertEquals("name", result.sort().property());
        assertEquals("DESC", result.sort().direction());
    }
}
