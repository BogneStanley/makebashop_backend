package cm.bognestanley.shop_backend.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import cm.bognestanley.shop_backend.domain.contactsettings.entity.ContactSettings;
import cm.bognestanley.shop_backend.domain.contactsettings.repository.ContactSettingsRepository;
import cm.bognestanley.shop_backend.infrastructure.persistence.mapper.ContactSettingsMapper;
import cm.bognestanley.shop_backend.infrastructure.persistence.repository.ContactSettingsJpaRepository;

@Repository
public class ContactSettingsRepositoryJpaAdapter implements ContactSettingsRepository {

    private final ContactSettingsJpaRepository contactSettingsJpaRepository;
    private final ContactSettingsMapper contactSettingsMapper;

    public ContactSettingsRepositoryJpaAdapter(
            ContactSettingsJpaRepository contactSettingsJpaRepository,
            ContactSettingsMapper contactSettingsMapper) {
        this.contactSettingsJpaRepository = contactSettingsJpaRepository;
        this.contactSettingsMapper = contactSettingsMapper;
    }

    @Override
    public Optional<ContactSettings> find() {
        return contactSettingsJpaRepository
                .findById(ContactSettings.SINGLETON_ID)
                .map(contactSettingsMapper::toDomain);
    }

    @Override
    public ContactSettings save(ContactSettings contactSettings) {
        return contactSettingsMapper.toDomain(
                contactSettingsJpaRepository.save(contactSettingsMapper.toJpaEntity(contactSettings)));
    }
}
