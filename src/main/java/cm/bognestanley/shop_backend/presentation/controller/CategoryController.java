package cm.bognestanley.shop_backend.presentation.controller;

import cm.bognestanley.shop_backend.presentation.dto.request.category.CategoryRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.category.CategoryResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.facade.CategoryFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryFacade categoryFacade;

    @GetMapping
    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDataWrapper<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> categoryResponse = categoryFacade.getCategories();
        return ResponseEntity.ok(ResponseDataWrapper.ok(categoryResponse, "CATEGORIES_RETRIEVED", "Categories retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDataWrapper<CategoryResponse>> getCategory(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryFacade.getCategory(id);
        return ResponseEntity.ok(ResponseDataWrapper.ok(categoryResponse, "CATEGORY_RETRIEVED", "Category retrieved successfully"));
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDataWrapper<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request
            ) {
        CategoryResponse response = categoryFacade.createCategory(request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(response, "CATEGORY_CREATED", "Category created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category Updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDataWrapper<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryFacade.updateCategory(id, request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(response, "CATEGORY_UPDATED", "Category created successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDataWrapper<?>> deleteCategory(
            @PathVariable Long id
    ) {
        categoryFacade.deleteCategory(id);
        return ResponseEntity.ok(ResponseDataWrapper.ok(null, "CATEGORY_DELETED", "Category deleted successfully"));
    }


}
