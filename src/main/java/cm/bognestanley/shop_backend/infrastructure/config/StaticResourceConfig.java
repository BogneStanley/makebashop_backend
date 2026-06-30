package cm.bognestanley.shop_backend.infrastructure.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private final UploadProperties uploadProperties;

    public StaticResourceConfig(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadProperties.getDir()).toAbsolutePath().normalize();
        String urlPattern = uploadProperties.getUrlPrefix().endsWith("/")
                ? uploadProperties.getUrlPrefix() + "**"
                : uploadProperties.getUrlPrefix() + "/**";

        registry.addResourceHandler(urlPattern)
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
