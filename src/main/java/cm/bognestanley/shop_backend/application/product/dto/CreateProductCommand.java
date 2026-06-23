package cm.bognestanley.shop_backend.application.product.dto;

import java.util.List;

import cm.bognestanley.shop_backend.application.common.dto.FileContent;

public record CreateProductCommand(
    String name,
    String description,
    List<CreateProductVariantCommand> productVariants,
    List<Long> categories,
    List<FileContent> images
) {}
