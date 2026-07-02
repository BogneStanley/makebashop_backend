package cm.bognestanley.shop_backend.domain.producthighlight.entity;

import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;

public class ProductHighlightSlot {

    private Long id;
    private HighlightListType listType;
    private Long productId;
    private int position;

    public ProductHighlightSlot(Long id, HighlightListType listType, Long productId, int position) {
        this.id = id;
        this.listType = listType;
        this.productId = productId;
        this.position = position;
    }

    public static ProductHighlightSlot create(HighlightListType listType, Long productId, int position) {
        return new ProductHighlightSlot(null, listType, productId, position);
    }

    public Long getId() {
        return id;
    }

    public HighlightListType getListType() {
        return listType;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPosition() {
        return position;
    }
}
