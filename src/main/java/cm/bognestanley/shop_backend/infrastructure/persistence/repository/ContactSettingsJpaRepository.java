package cm.bognestanley.shop_backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cm.bognestanley.shop_backend.infrastructure.persistence.entity.contactsettings.ContactSettingsJpaEntity;

public interface ContactSettingsJpaRepository extends JpaRepository<ContactSettingsJpaEntity, Long> {
}
