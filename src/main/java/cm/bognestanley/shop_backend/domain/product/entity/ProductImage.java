package cm.bognestanley.shop_backend.domain.product.entity;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;

public class ProductImage {
        private Long id;
        private String url;
        private String storageKey;
        private String contentType;
        private String fileName;
        private boolean isPrimary;
        private int position;

    public ProductImage(Long id, String url, String storageKey, String contentType, String fileName, boolean isPrimary, int position) {
        if (url == null || url.isBlank()) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Image URL cannot be null or empty");
        }
        this.id = id;
        this.url = url;
        this.storageKey = storageKey;
        this.contentType = contentType;
        this.fileName = fileName;
        this.isPrimary = isPrimary;
        this.position = position;
    }

    public void markAsPrimary() {
        this.isPrimary = true;
    }

    public void updatePosition(int newPosition) {
        this.position = newPosition;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void markAsNotPrimary() {
        this.isPrimary = false;
    }
}
