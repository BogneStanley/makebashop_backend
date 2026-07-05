package cm.bognestanley.shop_backend.application.contactsettings.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.contactsettings.dto.ContactSettingsResult;
import cm.bognestanley.shop_backend.domain.contactsettings.entity.ContactSettings;
import cm.bognestanley.shop_backend.domain.contactsettings.repository.ContactSettingsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetContactSettingsUsecase {

    private final ContactSettingsRepository contactSettingsRepository;

    public ContactSettingsResult execute() {
        ContactSettings settings = contactSettingsRepository.find()
                .orElseGet(() -> contactSettingsRepository.save(ContactSettings.createEmpty()));
        return toResult(settings);
    }

    static ContactSettingsResult toResult(ContactSettings settings) {
        return new ContactSettingsResult(settings.getContacts(), settings.getUpdatedAt());
    }
}
