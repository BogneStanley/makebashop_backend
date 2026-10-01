package cm.bognestanley.shop_backend.presentation;

import cm.bognestanley.shop_backend.IntegrationTestSupport;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.cart.CartItemJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.cart.CartJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.order.OrderStatusJpa;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.CartJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.OrderJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductJpaRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductVariantJpaRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CheckoutIntegrationTest extends IntegrationTestSupport {

    private static final String GUEST_TOKEN = "checkout-integration-guest";
    private static final String IDEMPOTENCY_KEY = "checkout-integration-key";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductJpaRepository productRepository;

    @Autowired
    private ProductVariantJpaRepository variantRepository;

    @Autowired
    private CartJpaRepository cartRepository;

    @Autowired
    private OrderJpaRepository orderRepository;

    @Test
    void checkoutReservesStockAndReplaysTheSameOrderForTheSameIdempotencyKey() throws Exception {
        ProductVariantJpaEntity variant = createCartWithPurchasableVariant();

        checkout()
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.items[0].quantity").value(2));

        assertThat(variantRepository.findById(variant.getId()).orElseThrow().getStockQuantity()).isEqualTo(1);
        assertThat(orderRepository.findByIdempotencyKey(IDEMPOTENCY_KEY))
                .hasValueSatisfying(order -> {
                    assertThat(order.getStatus()).isEqualTo(OrderStatusJpa.PENDING);
                    assertThat(order.getOrderLineItems()).hasSize(1);
                });

        checkout().andExpect(status().isCreated());

        assertThat(orderRepository.findAll()).hasSize(1);
        assertThat(variantRepository.findById(variant.getId()).orElseThrow().getStockQuantity()).isEqualTo(1);
    }

    private ProductVariantJpaEntity createCartWithPurchasableVariant() {
        LocalDateTime now = LocalDateTime.now();
        ProductJpaEntity product = ProductJpaEntity.builder()
                .name("Chemise de test")
                .description("Produit de test")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        product = productRepository.save(product);

        ProductVariantJpaEntity variant = ProductVariantJpaEntity.builder()
                .product(product)
                .sku("CHECKOUT-TEST-SKU")
                .currencyCode("FCFA")
                .price(new BigDecimal("12000.00"))
                .stockQuantity(3)
                .size("M")
                .color("Bleu")
                .createdAt(now)
                .updatedAt(now)
                .build();
        variant = variantRepository.save(variant);

        CartJpaEntity cart = new CartJpaEntity();
        cart.setGuestToken(GUEST_TOKEN);

        CartItemJpaEntity item = new CartItemJpaEntity();
        item.setCart(cart);
        item.setProduct(product);
        item.setProductVariant(variant);
        item.setQuantity(2);
        cart.getCartItems().add(item);
        cartRepository.save(cart);

        return variant;
    }

    private org.springframework.test.web.servlet.ResultActions checkout() throws Exception {
        return mockMvc.perform(post("/api/v1/orders/checkout")
                .cookie(new Cookie("guest_cart", GUEST_TOKEN))
                .with(csrf())
                .header("Idempotency-Key", IDEMPOTENCY_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Ada",
                          "lastName": "Lovelace",
                          "phoneNumber": "+237 699 000 000"
                        }
                        """));
    }
}
