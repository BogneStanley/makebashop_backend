package cm.bognestanley.shop_backend.presentation.facade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.order.dto.CreateOrderCommand;
import cm.bognestanley.shop_backend.application.order.dto.CustomerCommand;
import cm.bognestanley.shop_backend.application.order.usecase.*;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.domain.pagination.PaginationAttribute;
import cm.bognestanley.shop_backend.domain.pagination.SortEntity;
import cm.bognestanley.shop_backend.presentation.dto.request.order.CreateOrderRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.order.OrderResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresOrderMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final CreateOrderUsecase createOrderUsecase;
    private final GetAllOrderUsercase getAllOrdersUsecase;
    private final GetOneOrderUsecase getOrderUsecase;
    private final MarkOrderAsCancelledUsecase markOrderAsCancelledUsecase;
    private final MarkOrderAsPaidUsecase markOrderAsPaidUsecase;
    private final SearchOrderUsecase searchOrderUsecase;
    private final PresOrderMapper orderMapper;

    public PaginatedEntity<OrderResponse> findAllOrders(int page, int size, String sortBy, String sortOrder) {

        PaginationAttribute paginationAttribute = new PaginationAttribute(page, size,
                new SortEntity(sortBy, sortOrder));

        return getAllOrdersUsecase.execute(paginationAttribute).map(orderMapper::toOrderResponse);

    }

    public PaginatedEntity<OrderResponse> searchOrders(String keyword, LocalDateTime startDate,
            LocalDateTime endDate,
            String status, int page, int size, String sortBy, String sortOrder) {

        PaginationAttribute paginationAttribute = new PaginationAttribute(page, size,
                new SortEntity(sortBy, sortOrder));

        return searchOrderUsecase.execute(keyword, status, startDate, endDate,
                paginationAttribute).map(orderMapper::toOrderResponse);
    }

    public OrderResponse getOrderById(Long id) {
        return orderMapper.toOrderResponse(getOrderUsecase.execute(id));
    }

    public void markOrderAsCancelled(Long id) {
        markOrderAsCancelledUsecase.execute(id);
    }

    public void markOrderAsPaid(Long id) {
        markOrderAsPaidUsecase.execute(id);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        CreateOrderCommand command = new CreateOrderCommand(
                new CustomerCommand(
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        request.phoneNumber()),
                request.note());
        Order order = createOrderUsecase.execute(command);
        return orderMapper.toOrderResponse(order);
    }

}
