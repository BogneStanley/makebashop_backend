package cm.bognestanley.shop_backend.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record PaginatedEntity<E>(
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalElements,
        List<E> content,
        boolean first,
        boolean last,
        boolean empty,
        SortEntity sort) {

            public <T> PaginatedEntity<T> map(Function<E, T> mapper) {
                return new PaginatedEntity<>(
                    pageNumber,
                    pageSize,
                    totalPages,
                    totalElements,
                    content.stream().map(mapper).toList(),
                    first,
                    last,
                    empty,
                    sort
                );
            }

}
