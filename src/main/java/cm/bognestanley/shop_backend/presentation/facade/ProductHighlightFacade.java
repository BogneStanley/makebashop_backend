package cm.bognestanley.shop_backend.presentation.facade;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cm.bognestanley.shop_backend.application.producthighlight.dto.ProductHighlightConfigResult;
import cm.bognestanley.shop_backend.application.producthighlight.dto.SetProductHighlightListCommand;
import cm.bognestanley.shop_backend.application.producthighlight.usecase.ClearProductHighlightListUsecase;
import cm.bognestanley.shop_backend.application.producthighlight.usecase.GetProductHighlightConfigUsecase;
import cm.bognestanley.shop_backend.application.producthighlight.usecase.SetProductHighlightListUsecase;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import cm.bognestanley.shop_backend.infrastructure.security.CurrentUserProvider;
import cm.bognestanley.shop_backend.presentation.dto.request.producthighlight.SetProductHighlightListRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.producthighlight.ProductHighlightConfigResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.producthighlight.ProductHighlightListConfigResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductHighlightFacade {

    private final GetProductHighlightConfigUsecase getProductHighlightConfigUsecase;
    private final SetProductHighlightListUsecase setProductHighlightListUsecase;
    private final ClearProductHighlightListUsecase clearProductHighlightListUsecase;
    private final CurrentUserProvider currentUserProvider;
    private final PresProductMapper productMapper;

    public ProductHighlightConfigResponse getConfig() {
        requireAdmin();
        ProductHighlightConfigResult config = getProductHighlightConfigUsecase.execute();
        return new ProductHighlightConfigResponse(
                toListConfigResponse(config.lists().get(HighlightListType.NEW)),
                toListConfigResponse(config.lists().get(HighlightListType.POPULAR)),
                toListConfigResponse(config.lists().get(HighlightListType.FEATURED)));
    }

    public ProductHighlightConfigResponse setList(HighlightListType listType, SetProductHighlightListRequest request) {
        requireAdmin();
        setProductHighlightListUsecase.execute(new SetProductHighlightListCommand(listType, request.productIds()));
        return getConfig();
    }

    public ProductHighlightConfigResponse clearList(HighlightListType listType) {
        requireAdmin();
        clearProductHighlightListUsecase.execute(listType);
        return getConfig();
    }

    public HighlightListType parseListType(String type) {
        try {
            return HighlightListType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid list type. Allowed values: NEW, POPULAR, FEATURED");
        }
    }

    private ProductHighlightListConfigResponse toListConfigResponse(
            ProductHighlightConfigResult.ProductHighlightListConfig config) {
        return new ProductHighlightListConfigResponse(
                config.configured(),
                config.products().stream().map(productMapper::toResponse).toList());
    }

    private void requireAdmin() {
        if (!currentUserProvider.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can manage product highlights");
        }
    }
}
