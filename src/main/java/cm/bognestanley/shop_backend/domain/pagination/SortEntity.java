package cm.bognestanley.shop_backend.domain.pagination;

import java.io.Serializable;

public record SortEntity(String property, String direction) implements Serializable {
}
