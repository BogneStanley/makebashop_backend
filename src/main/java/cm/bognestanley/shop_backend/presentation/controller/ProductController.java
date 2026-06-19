package cm.bognestanley.shop_backend.presentation.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cm.bognestanley.shop_backend.domain.pagination.PaginatedEntity;
import cm.bognestanley.shop_backend.presentation.dto.request.product.AddVariantsRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.CreateProductRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.DeleteImagesRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.DeleteVariantsRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.ProductVariantRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.UpdateImagesPositionRequest;
import cm.bognestanley.shop_backend.presentation.dto.request.product.UpdateProductRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.product.ProductResponse;
import cm.bognestanley.shop_backend.presentation.facade.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product management")
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping()
    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<PaginatedEntity<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        PaginatedEntity<ProductResponse> paginatedEntity = productFacade.getAllProducts(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(ResponseDataWrapper.ok(paginatedEntity));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<PaginatedEntity<ProductResponse>>> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock) {
        PaginatedEntity<ProductResponse> paginatedEntity = productFacade.searchProducts(name, minPrice, maxPrice,
                inStock, page, size, sortBy, sortOrder);
        return ResponseEntity.ok(ResponseDataWrapper.ok(paginatedEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productFacade.getProduct(id);
        return ResponseEntity.ok(ResponseDataWrapper.ok(productResponse));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> createProduct(

            @Parameter(
                description = "Product JSON",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @Valid @RequestPart("product")
            CreateProductRequest request,
            
             @Parameter(
                description = "Product images",
                content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        ProductResponse productResponse = productFacade.createProduct(request, images);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDataWrapper.ok(productResponse, "PRODUCT_CREATED", "Product created successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<Void>> deleteProduct(@PathVariable Long id) {
        productFacade.deleteProduct(id);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(null, "PRODUCT_DELETED", "Product deleted successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse productResponse = productFacade.updateProduct(id, request);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_UPDATED", "Product updated successfully"));
    }

    @PutMapping("/{productId}/variants/{variantId}")
    @Operation(summary = "Update product variant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product variant updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product variant not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> updateVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantRequest request) {
        ProductResponse productResponse = productFacade.updateVariant(productId, variantId, request);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_VARIANT_UPDATED", "Product variant updated successfully"));
    }

    @PostMapping("/{productId}/variants")
    @Operation(summary = "Bulk add product variants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product variants added"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> addVariantsToProduct(
            @PathVariable Long productId,
            @Valid @RequestBody AddVariantsRequest requests) {
        ProductResponse productResponse = productFacade.addVariantsToProduct(productId, requests.variants());
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_VARIANTS_ADDED", "Product variants added successfully"));
    }

    @DeleteMapping("/{productId}/variants")
    @Operation(summary = "Delete product variant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product variant deleted"),
            @ApiResponse(responseCode = "404", description = "Product variant not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> deleteVariants(
            @PathVariable Long productId,
            @Valid @RequestBody DeleteVariantsRequest requests) {
        ProductResponse productResponse = productFacade.deleteVariantsFromProduct(productId, requests.variantIds());
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_VARIANT_DELETED", "Product variant deleted successfully"));
    }

    @DeleteMapping("/{productId}/images")
    @Operation(summary = "Delete product image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image deleted"),
            @ApiResponse(responseCode = "404", description = "Product image not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> deleteImages(
            @PathVariable Long productId,
            @Valid @RequestBody DeleteImagesRequest requests) {
        ProductResponse productResponse = productFacade.deleteImagesFromProduct(productId, requests.imageIds());
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_IMAGE_DELETED", "Product image deleted successfully"));
    }

    @PostMapping(value = "/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add images to product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product images added"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> addImages(
            @PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> images) {
        ProductResponse productResponse = productFacade.addImagesToProduct(productId, images);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_IMAGES_ADDED", "Product images added successfully"));
    }

    @PutMapping("/{productId}/images/positions")
    @Operation(summary = "Update product image positions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product image not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> updateImagePosition(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateImagesPositionRequest request) {
        ProductResponse productResponse = productFacade.updateImagesPosition(productId, request.imagesPosition());
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_IMAGE_POSITION_UPDATED", "Product image position updated successfully"));
    }

    @PutMapping("/{productId}/images/{imageId}/primary")
    @Operation(summary = "Set image as primary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image set as primary"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Product image not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ProductResponse>> setImageAsPrimary(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        ProductResponse productResponse = productFacade.setProductImageAsPrimary(productId, imageId);
        return ResponseEntity
                .ok(ResponseDataWrapper.ok(productResponse, "PRODUCT_IMAGE_SET_AS_PRIMARY", "Product image set as primary successfully"));
    }

}
