package cm.bognestanley.shop_backend.presentation.dto.response.common;

import java.math.BigDecimal;

public record MoneyResponse(
    BigDecimal amount,
    String currency
) {
}
