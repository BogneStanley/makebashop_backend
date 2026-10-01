package cm.bognestanley.shop_backend.presentation;

import cm.bognestanley.shop_backend.IntegrationTestSupport;
import cm.bognestanley.shop_backend.application.common.port.PasswordEncoderPort;
import cm.bognestanley.shop_backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiSecurityContractTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderPort passwordEncoder;

    @Test
    void protectedOrderEndpointReturnsTheStandardUnauthenticatedContract() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messageCode").value("UNAUTHENTICATED"))
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    void orderEndpointRejectsNonAdminUsersWithTheStandardForbiddenContract() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.messageCode").value("FORBIDDEN"))
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void checkoutMissingIdempotencyKeyReturnsValidationContract() throws Exception {
        mockMvc.perform(post("/api/v1/orders/checkout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ada",
                                  "lastName": "Lovelace",
                                  "phoneNumber": "+237 699 000 000"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors.request").isNotEmpty());
    }

    @Test
    void invalidPaginationReturnsValidationContractBeforeCallingTheCatalog() throws Exception {
        mockMvc.perform(get("/api/v1/products").param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors.page").isNotEmpty());
    }

    @Test
    void checkoutOpenApiContractDocumentsTheActualSuccessAndClientErrorStatuses() throws Exception {
        mockMvc.perform(get("/api/v1/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/orders/checkout'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/orders/checkout'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/orders/checkout'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/orders/checkout'].post.responses['200']").doesNotExist())
                .andExpect(jsonPath("$.paths['/api/v1/admin/users'].get.security[0].bearerAuth").exists())
                .andExpect(jsonPath("$.paths['/api/v1/categories'].get.security").doesNotExist());
    }

    @Test
    void initialSetupCreatesOneBcryptAdminThenMakesTheEndpointUnavailable() throws Exception {
        String password = "Str0ng-password!";

        mockMvc.perform(get("/api/v1/setup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.setupRequired").value(true));

        mockMvc.perform(post("/api/v1/setup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@example.com",
                                  "firstName": "Ada",
                                  "lastName": "Lovelace",
                                  "password": "Str0ng-password!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.messageCode").value("INITIAL_ADMIN_CREATED"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

        org.junit.jupiter.api.Assertions.assertTrue(passwordEncoder.matches(
                password, userRepository.findByEmail("admin@example.com").orElseThrow().getPassword()));

        mockMvc.perform(get("/api/v1/setup"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("SETUP_NOT_AVAILABLE"));

        mockMvc.perform(post("/api/v1/setup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "second-admin@example.com",
                                  "firstName": "Grace",
                                  "lastName": "Hopper",
                                  "password": "An0ther-strong-password!"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("SETUP_NOT_AVAILABLE"));
    }
}
