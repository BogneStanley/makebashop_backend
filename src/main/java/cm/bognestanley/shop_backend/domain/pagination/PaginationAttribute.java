package cm.bognestanley.shop_backend.domain.pagination;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;

public record PaginationAttribute(
    int pageNumber, 
    int pageSize, 
    SortEntity sort
) {
    
    public PaginationAttribute(int pageNumber, int pageSize, SortEntity sort) {
        if (pageNumber < 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Page number cannot be negative");
        }
        if (pageSize < 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Page size cannot be negative");
        }
        if (sort == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Sort cannot be null");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }
    
}
