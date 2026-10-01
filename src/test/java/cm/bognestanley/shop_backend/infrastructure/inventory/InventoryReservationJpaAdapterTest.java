package cm.bognestanley.shop_backend.infrastructure.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cm.bognestanley.shop_backend.domain.cart.entity.CartItem;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.valueObject.Money;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.entity.ProductVariant;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.product.ProductVariantJpaEntity;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.CategoryMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.ProductMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ProductVariantJpaRepository;

class InventoryReservationJpaAdapterTest {

    @Test
    void reservesAuthoritativeStockAndCreatesOrderSnapshots() {
        ProductVariantJpaRepository repository = Mockito.mock(ProductVariantJpaRepository.class);
        ProductVariantJpaEntity persistedVariant = persistedVariant(2);
        when(repository.findAllByIdInForUpdate(anyCollection())).thenReturn(List.of(persistedVariant));
        InventoryReservationJpaAdapter adapter = new InventoryReservationJpaAdapter(repository,
                new ProductMapper(Mockito.mock(CategoryMapper.class)));

        var lineItems = adapter.reserve(List.of(cartItem(2)));

        assertEquals(0, persistedVariant.getStockQuantity());
        assertEquals(new BigDecimal("2500"), lineItems.getFirst().getPrice().amount());
        verify(repository).saveAll(anyCollection());
    }

    @Test
    void rejectsOversellingBeforeCreatingAnOrder() {
        ProductVariantJpaRepository repository = Mockito.mock(ProductVariantJpaRepository.class);
        when(repository.findAllByIdInForUpdate(anyCollection())).thenReturn(List.of(persistedVariant(1)));
        InventoryReservationJpaAdapter adapter = new InventoryReservationJpaAdapter(repository,
                new ProductMapper(Mockito.mock(CategoryMapper.class)));

        assertThrows(DomainErrorException.class, () -> adapter.reserve(List.of(cartItem(2))));
    }

    private CartItem cartItem(int quantity) {
        ProductVariant variant = new ProductVariant(10L, "SKU-10", new Money(new BigDecimal("2500"), "FCFA"),
                99, null, null, LocalDateTime.now(), LocalDateTime.now());
        Product product = new Product(1L, "Produit", "Description", null, null, true,
                List.of(variant), List.of(), List.of(), LocalDateTime.now(), LocalDateTime.now());
        return new CartItem(1L, product, variant, quantity);
    }

    private ProductVariantJpaEntity persistedVariant(int stock) {
        ProductJpaEntity product = ProductJpaEntity.builder().id(1L).isActive(true).build();
        return ProductVariantJpaEntity.builder()
                .id(10L)
                .sku("SKU-10")
                .price(new BigDecimal("2500"))
                .currencyCode("FCFA")
                .stockQuantity(stock)
                .product(product)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
