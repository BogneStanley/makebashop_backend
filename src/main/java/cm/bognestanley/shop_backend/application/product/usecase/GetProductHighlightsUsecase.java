package cm.bognestanley.shop_backend.application.product.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.product.dto.ProductHighlightsResult;
import cm.bognestanley.shop_backend.application.producthighlight.service.ProductHighlightResolver;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProductHighlightsUsecase {

    private final ProductHighlightResolver productHighlightResolver;

    public ProductHighlightsResult execute() {
        return new ProductHighlightsResult(
                productHighlightResolver.resolve(HighlightListType.NEW, true),
                productHighlightResolver.resolve(HighlightListType.POPULAR, true),
                productHighlightResolver.resolve(HighlightListType.FEATURED, true));
    }

}
