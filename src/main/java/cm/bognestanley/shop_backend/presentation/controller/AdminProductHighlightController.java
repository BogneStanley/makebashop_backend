package cm.bognestanley.shop_backend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import cm.bognestanley.shop_backend.presentation.dto.request.producthighlight.SetProductHighlightListRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.producthighlight.ProductHighlightConfigResponse;
import cm.bognestanley.shop_backend.presentation.facade.ProductHighlightFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/product-highlights")
@RequiredArgsConstructor
@Tag(name = "Product Highlights (Admin)", description = "Configure homepage product highlight lists")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductHighlightController {

    private final ProductHighlightFacade productHighlightFacade;

    @GetMapping
    @Operation(summary = "Get product highlight configuration (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductHighlightConfigResponse>> getConfig() {
        ProductHighlightConfigResponse config = productHighlightFacade.getConfig();
        return ResponseEntity.ok(ResponseDataWrapper.ok(config, "PRODUCT_HIGHLIGHTS_RETRIEVED", "Product highlights retrieved"));
    }

    @PutMapping("/{type}")
    @Operation(summary = "Replace a highlight list (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductHighlightConfigResponse>> setList(
            @PathVariable String type,
            @Valid @RequestBody SetProductHighlightListRequest request) {
        HighlightListType listType = productHighlightFacade.parseListType(type);
        ProductHighlightConfigResponse config = productHighlightFacade.setList(listType, request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(config, "PRODUCT_HIGHLIGHTS_UPDATED", "Product highlights updated"));
    }

    @DeleteMapping("/{type}")
    @Operation(summary = "Clear a highlight list and revert to automatic selection (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration cleared"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin only", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductHighlightConfigResponse>> clearList(@PathVariable String type) {
        HighlightListType listType = productHighlightFacade.parseListType(type);
        ProductHighlightConfigResponse config = productHighlightFacade.clearList(listType);
        return ResponseEntity.ok(ResponseDataWrapper.ok(config, "PRODUCT_HIGHLIGHTS_CLEARED", "Product highlights cleared"));
    }
}
