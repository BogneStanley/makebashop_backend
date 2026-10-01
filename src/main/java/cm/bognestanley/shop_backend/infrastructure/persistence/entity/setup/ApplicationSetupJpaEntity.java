package cm.bognestanley.shop_backend.infrastructure.persistence.entity.setup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_setup")
@Getter
@NoArgsConstructor
public class ApplicationSetupJpaEntity {

    @Id
    private Short id;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void markCompleted() {
        completedAt = LocalDateTime.now();
    }
}
