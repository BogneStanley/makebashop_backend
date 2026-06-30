package cm.bognestanley.shop_backend.domain.product.criteria;

import java.math.BigDecimal;
import java.util.List;

public record ProductSearchCriteria(
    String name,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    Boolean inStock,
    Boolean isActive,
    List<Long> categoryIds
) {
    
}
