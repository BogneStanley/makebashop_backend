package cm.bognestanley.shop_backend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.contactsettings.ContactSettingsResponse;
import cm.bognestanley.shop_backend.presentation.facade.ContactSettingsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Informations de contact de la boutique (lecture publique)")
public class ContactSettingsController {

    private final ContactSettingsFacade contactSettingsFacade;

    @GetMapping
    @Operation(summary = "Récupérer les contacts de la boutique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacts récupérés")
    })
    public ResponseEntity<ResponseDataWrapper<ContactSettingsResponse>> getContactSettings() {
        ContactSettingsResponse response = contactSettingsFacade.getContactSettings();
        return ResponseEntity.ok(ResponseDataWrapper.ok(response, "CONTACT_SETTINGS_RETRIEVED", "Contact settings retrieved"));
    }
}
