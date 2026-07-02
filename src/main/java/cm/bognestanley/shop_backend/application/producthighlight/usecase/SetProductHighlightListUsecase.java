package cm.bognestanley.shop_backend.application.producthighlight.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.producthighlight.dto.SetProductHighlightListCommand;
import cm.bognestanley.shop_backend.application.producthighlight.service.ProductHighlightResolver;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import cm.bognestanley.shop_backend.domain.producthighlight.entity.ProductHighlightSlot;
import cm.bognestanley.shop_backend.domain.producthighlight.repository.ProductHighlightRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetProductHighlightListUsecase {

    private final ProductHighlightRepository productHighlightRepository;
    private final ProductRepository productRepository;

    public void execute(SetProductHighlightListCommand command) {
        if (command.listType() == null) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "List type cannot be null");
        }
        if (command.productIds() == null) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "Product ids cannot be null");
        }
        if (command.productIds().size() > ProductHighlightResolver.HIGHLIGHT_COUNT) {
            throw new ApplicationException(
                    ErrorCode.HIGHLIGHT_LIST_TOO_LARGE,
                    "Highlight list cannot contain more than " + ProductHighlightResolver.HIGHLIGHT_COUNT + " products");
        }

        Set<Long> uniqueIds = new java.util.HashSet<>();
        for (Long productId : command.productIds()) {
            if (productId == null) {
                throw new ApplicationException(ErrorCode.INVALID_INPUT, "Product id cannot be null");
            }
            if (!uniqueIds.add(productId)) {
                throw new ApplicationException(
                        ErrorCode.DUPLICATE_PRODUCT_IN_HIGHLIGHT_LIST,
                        "Duplicate product id in highlight list: " + productId);
            }
            productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(
                            ErrorCode.PRODUCT_NOT_FOUND,
                            "Product with ID " + productId + " not found"));
        }

        List<ProductHighlightSlot> slots = new ArrayList<>();
        for (int i = 0; i < command.productIds().size(); i++) {
            slots.add(ProductHighlightSlot.create(command.listType(), command.productIds().get(i), i + 1));
        }

        productHighlightRepository.replaceByListType(command.listType(), slots);
    }
}
