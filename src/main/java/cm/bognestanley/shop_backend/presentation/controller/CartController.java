package cm.bognestanley.shop_backend.presentation.controller;

import cm.bognestanley.shop_backend.presentation.dto.request.cart.AddToCartRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.cart.UpdateCartRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.cart.CartResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.facade.CartFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart management")
public class CartController {

    private final CartFacade cartFacade;

    @PostMapping
    @Operation(summary = "Add a product to the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<CartResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {

        CartResponse cartResponse = cartFacade.addToCart(request);

        return ResponseEntity
                .ok(ResponseDataWrapper.ok(cartResponse, "CART_PRODUCT_ADDED", "Product added successfully"));
    }

    @PutMapping
    @Operation(summary = "Update a product in the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<CartResponse>> updateCart(@Valid @RequestBody UpdateCartRequest request) {
        CartResponse cartResponse = cartFacade.updateCart(request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(cartResponse, "CART_UPDATED", "Cart updated successfully"));
    }

    @GetMapping
    @Operation(summary = "Get the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<CartResponse>> getCart() {
        CartResponse cartResponse = cartFacade.getCart();
        return ResponseEntity.ok(ResponseDataWrapper.ok(cartResponse, "CART_RETRIEVED", "Cart retrieved successfully"));
    }

    @DeleteMapping("/items/{variantId}")
    @Operation(summary = "Remove a product from the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<CartResponse>> removeFromCart(@PathVariable Long variantId) {
        CartResponse cartResponse = cartFacade.removeVariantToCart(variantId);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(cartResponse, "CART_ITEM_REMOVED", "Cart item removed successfully"));
    }

    @DeleteMapping
    @Operation(summary = "Clear the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<CartResponse>> clearCart() {
        CartResponse cartResponse = cartFacade.clearCart();
        return ResponseEntity.ok(ResponseDataWrapper.ok(cartResponse, "CART_CLEARED", "Cart cleared successfully"));
    }

}
