package cm.bognestanley.shop_backend.domain.product.entity;

import java.time.LocalDateTime;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.common.valueObject.Money;

public class ProductVariant {
    Long id;
    String sku;
    Money price;
    Integer stockQuantity;
    String size;
    String color;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public ProductVariant(Long id, String sku, Money price, Integer stockQuantity, String size, String color, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (sku == null || sku.isBlank()) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "SKU cannot be null or empty");
        }
        if (price == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Price cannot be null");
        }
        if (stockQuantity == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Stock quantity cannot be null");
        }
        if (stockQuantity < 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Stock quantity cannot be negative");
        }
        this.id = id;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.size = size;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductVariant create(String sku, Money price, Integer stockQuantity, String size, String color) {
        return new ProductVariant(null, sku, price, stockQuantity, size, color, LocalDateTime.now(), LocalDateTime.now());
    }

    public ProductVariant update(
        String sku,
        Money price,
        Integer stockQuantity,
        String size,
        String color
    ) {
        return new ProductVariant(id, sku, price, stockQuantity, size, color, createdAt, LocalDateTime.now());
    }

    public boolean isAvailable() {
        return stockQuantity > 0;
    }

    public boolean hasSufficientStock(int quantity) {
        return stockQuantity >= quantity;
    }

    public void updateStock(int newStockQuantity) {
        if (newStockQuantity < 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Stock quantity cannot be negative");
        }
        this.stockQuantity = newStockQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(int quantity) {
        if (quantity < 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Quantity to decrease cannot be negative");
        }
        if (!hasSufficientStock(quantity)) {
            throw new DomainErrorException(ErrorCode.OUT_OF_STOCK);
        }
        this.stockQuantity = stockQuantity - quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(int quantity) {
        if (quantity < 0) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Quantity to increase cannot be negative");
        }
        this.stockQuantity = stockQuantity + quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(Money newPrice) {
        if (newPrice == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Price cannot be null");
        }
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public Money getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
