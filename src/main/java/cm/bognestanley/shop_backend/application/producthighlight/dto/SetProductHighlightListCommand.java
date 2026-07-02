package cm.bognestanley.shop_backend.application.producthighlight.dto;

import java.util.List;

import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;

public record SetProductHighlightListCommand(
        HighlightListType listType,
        List<Long> productIds) {
}
