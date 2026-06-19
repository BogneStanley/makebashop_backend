package cm.bognestanley.shop_backend.domain.order.entity;

import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;

public class OrderLineItem {

    private Long id;
    private Product product;
    private ProductVariant productVariant;
    private int quantity;
    private Money price;
    
    public OrderLineItem(Long id, Product product, ProductVariant productVariant, int quantity, Money price) {
        this.id = id;
        this.product = product;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderLineItem create(Product product, ProductVariant productVariant, int quantity){
        return new OrderLineItem(null, product, productVariant, quantity, productVariant.getPrice());
    }

    public Money subtotal() {
        return price.multiply(quantity);
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    
}
