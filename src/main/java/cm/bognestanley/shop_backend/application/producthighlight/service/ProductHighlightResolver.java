package cm.bognestanley.shop_backend.application.producthighlight.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import cm.bognestanley.shop_backend.domain.producthighlight.entity.ProductHighlightSlot;
import cm.bognestanley.shop_backend.domain.producthighlight.repository.ProductHighlightRepository;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductHighlightResolver {

    public static final int HIGHLIGHT_COUNT = 4;

    private final ProductHighlightRepository productHighlightRepository;
    private final ProductRepository productRepository;

    public List<Product> resolve(HighlightListType listType, boolean activeOnly) {
        if (productHighlightRepository.isConfigured(listType)) {
            return resolveConfigured(listType, activeOnly);
        }
        return resolveAutomatic(listType, activeOnly);
    }

    private List<Product> resolveConfigured(HighlightListType listType, boolean activeOnly) {
        List<ProductHighlightSlot> slots = productHighlightRepository.findByListTypeOrderByPosition(listType);
        if (slots.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = slots.stream().map(ProductHighlightSlot::getProductId).toList();
        Map<Long, Product> productsById = productRepository.findAllByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return productIds.stream()
                .map(productsById::get)
                .filter(Objects::nonNull)
                .filter(product -> !activeOnly || product.isActive())
                .limit(HIGHLIGHT_COUNT)
                .toList();
    }

    private List<Product> resolveAutomatic(HighlightListType listType, boolean activeOnly) {
        return switch (listType) {
            case NEW -> productRepository.findNewest(HIGHLIGHT_COUNT, activeOnly);
            case POPULAR -> productRepository.findMostPopular(HIGHLIGHT_COUNT, activeOnly);
            case FEATURED -> List.of();
        };
    }

    public boolean isConfigured(HighlightListType listType) {
        return productHighlightRepository.isConfigured(listType);
    }

    public List<Product> loadProductsForSlots(List<ProductHighlightSlot> slots) {
        if (slots.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = slots.stream().map(ProductHighlightSlot::getProductId).toList();
        Map<Long, Product> productsById = productRepository.findAllByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return productIds.stream()
                .map(productsById::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
