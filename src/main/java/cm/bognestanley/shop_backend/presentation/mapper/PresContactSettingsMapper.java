package cm.bognestanley.shop_backend.presentation.mapper;

import org.springframework.stereotype.Component;

import cm.bognestanley.shop_backend.application.contactsettings.dto.ContactSettingsResult;
import cm.bognestanley.shop_backend.application.contactsettings.dto.UpdateContactSettingsCommand;
import cm.bognestanley.shop_backend.presentation.dto.request.contactsettings.UpdateContactSettingsRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.contactsettings.ContactSettingsResponse;

@Component
public class PresContactSettingsMapper {

    public ContactSettingsResponse toResponse(ContactSettingsResult result) {
        return new ContactSettingsResponse(result.contacts(), result.updatedAt());
    }

    public UpdateContactSettingsCommand toCommand(UpdateContactSettingsRequest request) {
        return new UpdateContactSettingsCommand(request.contacts());
    }
}
