package cm.bognestanley.shop_backend.infrastructure.persistence.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @Column(name = "storage_key")
    private String storageKey;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "is_primary")
    private boolean isPrimary;
    private int position;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductJpaEntity product;
}
