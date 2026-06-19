package cm.bognestanley.shop_backend.domain.pagination;

public record PaginationAttribute(
    int pageNumber, 
    int pageSize, 
    SortEntity sort
) {
    
    public PaginationAttribute(int pageNumber, int pageSize, SortEntity sort) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (pageSize < 0) {
            throw new IllegalArgumentException("Page size cannot be negative");
        }
        if (sort == null) {
            throw new IllegalArgumentException("Sort cannot be null");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }
    
}
