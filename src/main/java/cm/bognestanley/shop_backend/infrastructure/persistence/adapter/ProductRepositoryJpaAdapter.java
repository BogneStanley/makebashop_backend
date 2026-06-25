package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.product.criteria.ProductSearchCriteria;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.ProductMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.specidication.ProductSpecification;

import java.util.Optional;
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
    private final ProductMapper productMapper;

    public ProductRepositoryJpaAdapter(ProductJpaRepository productJpaRepository, ProductMapper productMapper) {
        this.productJpaRepository = productJpaRepository;
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
    public PaginatedEntity<Product> findAll(PaginationAttribute paginationAttribute, Boolean isActive) {
        Pageable pageable = PageRequest.of(
                paginationAttribute.pageNumber(),
                paginationAttribute.pageSize(),
                Sort.by(Direction.fromString(paginationAttribute.sort().direction()), paginationAttribute.sort().property()));
        Page<ProductJpaEntity> page = isActive == null
                ? productJpaRepository.findAll(pageable)
                : productJpaRepository.findAll(ProductSpecification.hasActiveStatus(isActive), pageable);
        return productMapper.toPaginatedDomain(page);
    }

    @Override
    public PaginatedEntity<Product> search(String name, Money minPrice, Money maxPrice, Boolean inStock, Boolean isActive,
            PaginationAttribute paginationAttribute) {

        Pageable pageable = PageRequest.of(
                paginationAttribute.pageNumber(),
                paginationAttribute.pageSize(),
                Sort.by(Direction.fromString(paginationAttribute.sort().direction()), paginationAttribute.sort().property()));

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria(
            name, 
            minPrice == null ? null : minPrice.amount(), 
            maxPrice == null ? null : maxPrice.amount(), 
            inStock,
            isActive
        );
        
        Specification<ProductJpaEntity> productSpec = ProductSpecification.toSpecification(productSearchCriteria);
        Page<ProductJpaEntity> page = productJpaRepository.findAll(productSpec, pageable);

        return productMapper.toPaginatedDomain(page);
    }

}
