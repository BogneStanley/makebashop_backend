package cm.bognestanley.shop_backend.infrastructure.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import cm.bognestanley.shop_backend.infrastructure.config.GuestCartCookieProperties;

class GuestCartCookieServiceTest {

    @Test
    void createsHighEntropyHttpOnlyCookie() {
        GuestCartCookieProperties properties = new GuestCartCookieProperties();
        properties.setSecure(true);
        GuestCartCookieService service = new GuestCartCookieService(properties);

        String firstToken = service.generateToken();
        String secondToken = service.generateToken();
        MockHttpServletResponse response = new MockHttpServletResponse();
        service.writeToken(response, firstToken);

        String cookie = response.getHeader("Set-Cookie");
        assertEquals(43, firstToken.length());
        assertNotEquals(firstToken, secondToken);
        assertTrue(cookie.contains("guest_cart=" + firstToken));
        assertTrue(cookie.contains("HttpOnly"));
        assertTrue(cookie.contains("Secure"));
        assertTrue(cookie.contains("SameSite=Lax"));
    }
}
