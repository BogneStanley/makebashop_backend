package cm.bognestanley.shop_backend.domain.pagination;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;

public record SortEntity(String property, String direction) {
    public SortEntity {
        if (property == null || property.isBlank()) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Property cannot be null or empty");
        }
        if (direction == null || direction.isBlank()) {
            direction = "asc";
        }
    }

}
