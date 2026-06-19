package cm.bognestanley.shop_backend.presentation.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.presentation.dto.request.order.CreateOrderRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.order.OrderResponse;
import cm.bognestanley.shop_backend.presentation.facade.OrderFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order API")
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping
    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<PaginatedEntity<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        return ResponseEntity.ok(ResponseDataWrapper.ok(
                orderFacade.findAllOrders(page, size, sortBy, sortOrder)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<OrderResponse>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseDataWrapper.ok(
                orderFacade.getOrderById(id)));
    }

    // search
    @GetMapping("/search")
    @Operation(summary = "Search orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<PaginatedEntity<OrderResponse>>> searchOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String query) {

        return ResponseEntity.ok(ResponseDataWrapper.ok(
                orderFacade.searchOrders(query, startDate, endDate, status, page, size, sortBy, sortOrder)));
    }

    // create
    @PostMapping("/checkout")
    @Operation(summary = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(ResponseDataWrapper.ok(
                orderFacade.createOrder(request)));
    }

    @PutMapping("{orderId}/paid")
    @Operation(summary = "Mark order as paid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as paid"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<?>> markAsPaid(@PathVariable Long orderId) {
        orderFacade.markOrderAsPaid(orderId);
        return ResponseEntity.ok(ResponseDataWrapper.ok(
                null,
                "ORDER_PAID",
                "Order marked as paid successfully"));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Mark order as cancelled")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as cancelled"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<?>> markAsCancelled(@PathVariable Long orderId) {
        orderFacade.markOrderAsCancelled(orderId);
        return ResponseEntity.ok(ResponseDataWrapper.ok(
                null,
                "ORDER_CANCELLED",
                "Order cancelled successfully"));
    }

}
