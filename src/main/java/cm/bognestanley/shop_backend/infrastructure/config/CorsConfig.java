package cm.bognestanley.shop_backend.infrastructure.config;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({
        UploadProperties.class,
        AppCorsProperties.class,
        AuthCookieProperties.class,
        AdminSeedProperties.class
})
public class CorsConfig implements WebMvcConfigurer {

    private final AppCorsProperties corsProperties;

    public CorsConfig(AppCorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    private List<String> getSanitizedOrigins() {
        if (corsProperties.getAllowedOrigins() == null) {
            return List.of();
        }
        return corsProperties.getAllowedOrigins().stream()
                .filter(origin -> origin != null && !origin.isBlank())
                .map(String::trim)
                .map(origin -> origin.replaceAll("/+$", ""))
                .toList();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = getSanitizedOrigins();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = getSanitizedOrigins();
        registry.addMapping("/**")
                .allowedOrigins(origins.toArray(String[]::new))
                .allowedMethods(corsProperties.getAllowedMethods().toArray(String[]::new))
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

