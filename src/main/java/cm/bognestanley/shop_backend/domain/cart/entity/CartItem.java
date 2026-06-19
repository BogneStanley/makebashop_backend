package cm.bognestanley.shop_backend.domain.cart.entity;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;

public class CartItem {
    private Long id;
    private Product product;
    private ProductVariant productVariant;
    private Integer quantity;

    public CartItem(
        Long id,
        Product product,
        ProductVariant productVariant,
        Integer quantity
    ){
        this.id = id;
        this.product = product;
        this.productVariant = productVariant;
        this.quantity = quantity;
    }

    public static CartItem create(Product product, ProductVariant productVariant, Integer quantity){
        if (quantity <= 0) {
            throw new DomainErrorException(ErrorCode.QUANTITY_SHOULD_BE_POSITIVE,"Quantity must be positive");
        }
        return new CartItem(null, product, productVariant, quantity);
    }

    public void increaseQuantity(Integer quantity){
        if (quantity <= 0) {
            throw new DomainErrorException(ErrorCode.QUANTITY_SHOULD_BE_POSITIVE,"Quantity must be positive");
        }
        this.quantity += quantity;
    }

    public void decreaseQuantity(Integer quantity){
        if (quantity <= 0) {
            throw new DomainErrorException(ErrorCode.QUANTITY_SHOULD_BE_POSITIVE,"Quantity must be positive");
        }
        this.quantity -= quantity;
    }

    public void updateQuantity(Integer quantity){
        if (quantity <= 0) {
            throw new DomainErrorException(ErrorCode.QUANTITY_SHOULD_BE_POSITIVE,"Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public Money getSubtotal(){
        return this.productVariant.getPrice().multiply(this.quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
}
