package cm.bognestanley.shop_backend.domain.producthighlight.repository;

import java.util.List;
import java.util.Map;

import cm.bognestanley.shop_backend.domain.producthighlight.entity.ProductHighlightSlot;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;

public interface ProductHighlightRepository {

    List<ProductHighlightSlot> findByListTypeOrderByPosition(HighlightListType listType);

    Map<HighlightListType, List<ProductHighlightSlot>> findAllGroupedByType();

    boolean isConfigured(HighlightListType listType);

    void replaceByListType(HighlightListType listType, List<ProductHighlightSlot> slots);

    void deleteByListType(HighlightListType listType);
}
