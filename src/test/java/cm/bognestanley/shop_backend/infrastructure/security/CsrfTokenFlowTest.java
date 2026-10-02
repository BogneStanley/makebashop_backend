package cm.bognestanley.shop_backend.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import jakarta.servlet.http.Cookie;

class CsrfTokenFlowTest {

    @Test
    void writesJavascriptReadableCookie() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        CsrfToken token = repository.generateToken(request);

        repository.saveToken(token, request, response);

        String setCookie = response.getHeader("Set-Cookie");
        assertNotNull(setCookie);
        assertFalse(setCookie.contains("HttpOnly"));
    }

    @Test
    void acceptsSameRawTokenInCookieAndHeader() throws Exception {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
        CsrfToken rawToken = repository.generateToken(new MockHttpServletRequest());

        MockHttpServletRequest protectedRequest = new MockHttpServletRequest("PUT", "/api/v1/admin/contact-settings");
        protectedRequest.setCookies(new Cookie("XSRF-TOKEN", rawToken.getToken()));
        protectedRequest.addHeader(rawToken.getHeaderName(), rawToken.getToken());
        MockHttpServletResponse protectedResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        CsrfFilter csrfFilter = new CsrfFilter(repository);
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        csrfFilter.setRequestHandler(requestHandler);
        csrfFilter.doFilter(protectedRequest, protectedResponse, filterChain);

        assertNotNull(filterChain.getRequest());
    }
}
