package cm.bognestanley.shop_backend.application.contactsettings.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.application.contactsettings.dto.ContactSettingsResult;
import cm.bognestanley.shop_backend.application.contactsettings.dto.UpdateContactSettingsCommand;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;
import cm.bognestanley.shop_backend.domain.contactsettings.entity.ContactSettings;
import cm.bognestanley.shop_backend.domain.contactsettings.repository.ContactSettingsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateContactSettingsUsecase {

    private final ContactSettingsRepository contactSettingsRepository;

    public ContactSettingsResult execute(UpdateContactSettingsCommand command) {
        if (command.contacts() == null || command.contacts().isEmpty()) {
            throw new ApplicationException(ErrorCode.INVALID_INPUT, "At least one contact entry is required");
        }

        ContactSettings settings = contactSettingsRepository.find()
                .orElseGet(ContactSettings::createEmpty);
        settings.mergeContacts(command.contacts());
        ContactSettings saved = contactSettingsRepository.save(settings);
        return GetContactSettingsUsecase.toResult(saved);
    }
}
