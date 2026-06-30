package cm.bognestanley.shop_backend.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {

    private String dir = "uploads";
    private String urlPrefix = "/uploads";
    private String publicBaseUrl = "http://localhost:8080";

    public String relativePath(String filename) {
        return normalizedUrlPrefix() + "/" + filename;
    }

    public String publicUrl(String filename) {
        return normalizedBaseUrl() + relativePath(filename);
    }

    public String resolvePublicUrl(String urlOrPath) {
        if (urlOrPath == null || urlOrPath.isBlank()) {
            return urlOrPath;
        }
        if (urlOrPath.startsWith("http://") || urlOrPath.startsWith("https://")) {
            return urlOrPath;
        }
        if (urlOrPath.startsWith("/")) {
            return normalizedBaseUrl() + urlOrPath;
        }
        return publicUrl(urlOrPath);
    }

    private String normalizedBaseUrl() {
        return publicBaseUrl.endsWith("/")
                ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                : publicBaseUrl;
    }

    private String normalizedUrlPrefix() {
        String prefix = urlPrefix.endsWith("/") ? urlPrefix.substring(0, urlPrefix.length() - 1) : urlPrefix;
        return prefix.startsWith("/") ? prefix : "/" + prefix;
    }
}
