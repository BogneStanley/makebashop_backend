package cm.bognestanley.shop_backend.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.domain.contactsettings.entity.ContactSettings;
import cm.bognestanley.shop_backend.infrastructure.persistence.entity.contactsettings.ContactSettingsJpaEntity;

@Component
public class ContactSettingsMapper {

    public ContactSettingsJpaEntity toJpaEntity(ContactSettings contactSettings) {
        return ContactSettingsJpaEntity.builder()
                .id(contactSettings.getId())
                .contacts(contactSettings.getContacts())
                .updatedAt(contactSettings.getUpdatedAt())
                .build();
    }

    public ContactSettings toDomain(ContactSettingsJpaEntity entity) {
        return new ContactSettings(entity.getId(), entity.getContacts(), entity.getUpdatedAt());
    }
}
