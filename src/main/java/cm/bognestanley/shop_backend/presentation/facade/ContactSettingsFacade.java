package cm.bognestanley.shop_backend.presentation.facade;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cm.bognestanley.shop_backend.application.contactsettings.usecase.GetContactSettingsUsecase;
import cm.bognestanley.shop_backend.application.contactsettings.usecase.UpdateContactSettingsUsecase;
import cm.bognestanley.shop_backend.infrastructure.security.CurrentUserProvider;
import cm.bognestanley.shop_backend.presentation.dto.request.contactsettings.UpdateContactSettingsRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.contactsettings.ContactSettingsResponse;
import cm.bognestanley.shop_backend.presentation.mapper.PresContactSettingsMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactSettingsFacade {

    private final GetContactSettingsUsecase getContactSettingsUsecase;
    private final UpdateContactSettingsUsecase updateContactSettingsUsecase;
    private final CurrentUserProvider currentUserProvider;
    private final PresContactSettingsMapper contactSettingsMapper;

    public ContactSettingsResponse getContactSettings() {
        return contactSettingsMapper.toResponse(getContactSettingsUsecase.execute());
    }

    public ContactSettingsResponse updateContactSettings(UpdateContactSettingsRequest request) {
        requireAdmin();
        return contactSettingsMapper.toResponse(
                updateContactSettingsUsecase.execute(contactSettingsMapper.toCommand(request)));
    }

    private void requireAdmin() {
        if (!currentUserProvider.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can manage contact settings");
        }
    }
}
