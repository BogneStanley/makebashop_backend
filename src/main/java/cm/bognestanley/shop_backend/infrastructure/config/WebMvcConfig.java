package cm.bognestanley.shop_backend.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiProperties apiProperties;

    public WebMvcConfig(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(apiProperties.getBasePath(),
                clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}
