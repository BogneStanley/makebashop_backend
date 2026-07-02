package cm.bognestanley.shop_backend.application.producthighlight.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.producthighlight.repository.ProductHighlightRepository;
import cm.bognestanley.shop_backend.domain.producthighlight.valueObject.HighlightListType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClearProductHighlightListUsecase {

    private final ProductHighlightRepository productHighlightRepository;

    public void execute(HighlightListType listType) {
        if (listType == null) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "List type cannot be null");
        }
        productHighlightRepository.deleteByListType(listType);
    }
}
