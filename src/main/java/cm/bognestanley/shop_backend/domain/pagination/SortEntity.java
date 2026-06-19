package cm.bognestanley.shop_backend.domain.pagination;

public record SortEntity(String property, String direction) {
    public SortEntity {
        if (property == null || property.isBlank()) {
            throw new IllegalArgumentException("Property cannot be null or empty");
        }
        if (direction == null || direction.isBlank()) {
            direction = "asc";
        }
    }

}
