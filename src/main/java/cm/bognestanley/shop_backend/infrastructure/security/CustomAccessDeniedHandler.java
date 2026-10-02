package cm.bognestanley.shop_backend.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication == null
                ? List.of()
                : authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Long userId = authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails
                ? userDetails.getUserId()
                : null;
        boolean csrfFailure = accessDeniedException instanceof CsrfException;

        String csrfHeader = request.getHeader("X-XSRF-TOKEN");
        String csrfCookie = csrfCookieValue(request);

        log.warn(
                "Access denied: method={}, path={}, reason={}, csrfHeaderPresent={}, csrfCookiePresent={}, csrfHeaderMatchesCookie={}, authenticated={}, userId={}, authorities={}",
                request.getMethod(),
                request.getRequestURI(),
                csrfFailure ? "CSRF" : "AUTHORIZATION",
                csrfHeader != null,
                csrfCookie != null,
                csrfHeader != null && csrfHeader.equals(csrfCookie),
                authentication != null && authentication.isAuthenticated(),
                userId,
                authorities);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(csrfFailure
                ? """
                        {"errors":null,"messageCode":"CSRF_TOKEN_INVALID","error":"The CSRF token is missing or invalid"}
                        """
                : """
                        {"errors":null,"messageCode":"FORBIDDEN","error":"You don't have permission to access this resource"}
                        """);
    }

    private String csrfCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("XSRF-TOKEN".equals(cookie.getName()) && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
