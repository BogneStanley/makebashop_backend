package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import cm.bognestanley.shop_backend.domain.order.criteria.OrderSearchCriteria;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.OrderMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.OrderJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.specidication.OrderSpecification;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class OrderProductRepositoryJpaAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    public OrderProductRepositoryJpaAdapter(OrderJpaRepository orderJpaRepository, OrderMapper orderMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(orderMapper::toDomain);
    }

    @Override
    public PaginatedEntity<Order> findAll(PaginationAttribute paginationAttribute) {
        Pageable pageable = PageRequest.of(
                paginationAttribute.pageNumber(),
                paginationAttribute.pageSize(),
                Sort.by(Direction.fromString(paginationAttribute.sort().direction()),
                        paginationAttribute.sort().property()));
        Page<OrderJpaEntity> page = orderJpaRepository.findAll(pageable);
        return orderMapper.toPaginatedDomain(page);
    }

    @Override
    public PaginatedEntity<Order> search(String orderNumber, String status, LocalDateTime startDate,
            LocalDateTime endDate, PaginationAttribute paginationAttribute) {
        OrderSearchCriteria searchCriteria = new OrderSearchCriteria(orderNumber, status, startDate, endDate);
        Pageable pageable = PageRequest.of(
                paginationAttribute.pageNumber(),
                paginationAttribute.pageSize(),
                Sort.by(Direction.fromString(paginationAttribute.sort().direction()),
                        paginationAttribute.sort().property()));
        Page<OrderJpaEntity> page = orderJpaRepository.findAll(OrderSpecification.toSpecification(searchCriteria),
                pageable);
        return orderMapper.toPaginatedDomain(page);
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity orderJpaEntity = orderMapper.toJpa(order);
        OrderJpaEntity saved = orderJpaRepository.save(orderJpaEntity);
        return orderMapper.toDomain(saved);
    }

}
