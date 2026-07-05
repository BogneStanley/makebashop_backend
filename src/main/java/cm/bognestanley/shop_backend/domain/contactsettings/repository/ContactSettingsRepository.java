package cm.bognestanley.shop_backend.domain.contactsettings.repository;

import java.util.Optional;

import cm.bognestanley.shop_backend.domain.contactsettings.entity.ContactSettings;

public interface ContactSettingsRepository {

    Optional<ContactSettings> find();

    ContactSettings save(ContactSettings contactSettings);
}
