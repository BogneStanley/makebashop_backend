package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.product.criteria.ProductSearchCriteria;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.ProductMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderStatusJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.OrderLineItemJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.specidication.ProductSpecification;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryJpaAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final OrderLineItemJpaRepository orderLineItemJpaRepository;
    private final ProductMapper productMapper;

    public ProductRepositoryJpaAdapter(
            ProductJpaRepository productJpaRepository,
            OrderLineItemJpaRepository orderLineItemJpaRepository,
            ProductMapper productMapper) {
        this.productJpaRepository = productJpaRepository;
        this.orderLineItemJpaRepository = orderLineItemJpaRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity productJpaEntity = productMapper.toJpa(product);

        return productMapper.toDomain(productJpaRepository.save(productJpaEntity));
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(productMapper.toJpa(product));
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return productJpaRepository.findAllById(ids).stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public PaginatedEntity<Product> findAll(PaginationAttribute paginationAttribute, Boolean isActive) {
        String sortBy = paginationAttribute.sort().property();
        String sortOrder = paginationAttribute.sort().direction();
        boolean isSortByPrice = "price".equalsIgnoreCase(sortBy);

        Pageable pageable = createPageable(paginationAttribute, isSortByPrice);

        ProductSearchCriteria criteria = new ProductSearchCriteria(
                null, null, null, null, isActive, null
        );

        Specification<ProductJpaEntity> spec = ProductSpecification.toSpecification(criteria, sortBy, sortOrder);
        Page<ProductJpaEntity> page = productJpaRepository.findAll(spec, pageable);
        return productMapper.toPaginatedDomain(page);
    }

    @Override
    public PaginatedEntity<Product> search(String name, Money minPrice, Money maxPrice, Boolean inStock, Boolean isActive,
            List<Long> categoryIds, PaginationAttribute paginationAttribute) {

        String sortBy = paginationAttribute.sort().property();
        String sortOrder = paginationAttribute.sort().direction();
        boolean isSortByPrice = "price".equalsIgnoreCase(sortBy);

        Pageable pageable = createPageable(paginationAttribute, isSortByPrice);

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria(
            name, 
            minPrice == null ? null : minPrice.amount(), 
            maxPrice == null ? null : maxPrice.amount(), 
            inStock,
            isActive,
            categoryIds
        );
        
        Specification<ProductJpaEntity> productSpec = ProductSpecification.toSpecification(productSearchCriteria, sortBy, sortOrder);
        Page<ProductJpaEntity> page = productJpaRepository.findAll(productSpec, pageable);

        return productMapper.toPaginatedDomain(page);
    }

    private Pageable createPageable(PaginationAttribute paginationAttribute, boolean isSortByPrice) {
        if (isSortByPrice) {
            return PageRequest.of(paginationAttribute.pageNumber(), paginationAttribute.pageSize());
        }
        return PageRequest.of(
                paginationAttribute.pageNumber(),
                paginationAttribute.pageSize(),
                Sort.by(Direction.fromString(paginationAttribute.sort().direction()), paginationAttribute.sort().property()));
    }

    @Override
    public List<Product> findNewest(int limit, boolean activeOnly) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Direction.DESC, "createdAt"));
        Page<ProductJpaEntity> page = activeOnly
                ? productJpaRepository.findAll(ProductSpecification.hasActiveStatus(true), pageable)
                : productJpaRepository.findAll(pageable);
        return page.getContent().stream().map(productMapper::toDomain).toList();
    }

    @Override
    public List<Product> findMostPopular(int limit, boolean activeOnly) {
        if (!activeOnly) {
            throw new UnsupportedOperationException("Popular products without active filter is not supported");
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<Long> productIds = orderLineItemJpaRepository.findMostPopularProductIds(OrderStatusJpa.PAID, pageable);
        if (productIds.isEmpty()) {
            return List.of();
        }

        Map<Long, Product> productsById = productJpaRepository.findAllById(productIds).stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return productIds.stream()
                .map(productsById::get)
                .filter(Objects::nonNull)
                .toList();
    }

}
