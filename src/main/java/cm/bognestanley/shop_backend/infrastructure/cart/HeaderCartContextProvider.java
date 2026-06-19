package cm.bognestanley.shop_backend.infrastructure.cart;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class HeaderCartContextProvider {

    Optional<Long> getCartIdFromHeader() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String cartIdHeader = request.getHeader("X-Cart-Id");
        return Optional.ofNullable(cartIdHeader)
                .map(Long::valueOf);

    }

}
