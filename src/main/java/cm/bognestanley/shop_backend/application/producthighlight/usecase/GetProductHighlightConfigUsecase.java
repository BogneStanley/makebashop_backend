package cm.bognestanley.shop_backend.application.producthighlight.usecase;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.producthighlight.dto.ProductHighlightConfigResult;
import cm.bognestanley.shop_backend.application.producthighlight.dto.ProductHighlightConfigResult.ProductHighlightListConfig;
import cm.bognestanley.shop_backend.application.producthighlight.service.ProductHighlightResolver;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.producthighlight.entity.ProductHighlightSlot;
import cm.bognestanley.shop_backend.domain.producthighlight.repository.ProductHighlightRepository;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProductHighlightConfigUsecase {

    private final ProductHighlightRepository productHighlightRepository;
    private final ProductHighlightResolver productHighlightResolver;

    public ProductHighlightConfigResult execute() {
        Map<HighlightListType, List<ProductHighlightSlot>> grouped = productHighlightRepository.findAllGroupedByType();
        Map<HighlightListType, ProductHighlightListConfig> lists = new EnumMap<>(HighlightListType.class);

        for (HighlightListType type : HighlightListType.values()) {
            List<ProductHighlightSlot> slots = grouped.get(type);
            boolean configured = productHighlightRepository.isConfigured(type);
            List<Product> products = productHighlightResolver.loadProductsForSlots(slots);
            lists.put(type, new ProductHighlightListConfig(configured, products));
        }

        return new ProductHighlightConfigResult(lists);
    }
}
