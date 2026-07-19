package cm.bognestanley.shop_backend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.presentation.dto.request.contactsettings.UpdateContactSettingsRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.contactsettings.ContactSettingsResponse;
import cm.bognestanley.shop_backend.presentation.facade.ContactSettingsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/contact-settings")
@RequiredArgsConstructor
@Tag(name = "Contact Settings (Admin)", description = "Configurer les contacts de la boutique")
@SecurityRequirement(name = "bearerAuth")
public class AdminContactSettingsController {

    private final ContactSettingsFacade contactSettingsFacade;

    @GetMapping
    @Operation(summary = "Récupérer la configuration des contacts (Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration récupérée"),
            @ApiResponse(responseCode = "403", description = "Accès refusé", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ContactSettingsResponse>> getContactSettings() {
        ContactSettingsResponse response = contactSettingsFacade.getContactSettings();
        return ResponseEntity.ok(ResponseDataWrapper.ok(response, "CONTACT_SETTINGS_RETRIEVED", "Contact settings retrieved"));
    }

    @PutMapping
    @Operation(summary = "Mettre à jour les contacts (Admin)", description = "Mise à jour partielle : seules les clés envoyées sont modifiées. Valeur vide supprime l'entrée. Nouvelles clés acceptées librement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration mise à jour"),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "403", description = "Accès refusé", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<ContactSettingsResponse>> updateContactSettings(
            @Valid @RequestBody UpdateContactSettingsRequest request) {
        ContactSettingsResponse response = contactSettingsFacade.updateContactSettings(request);
        return ResponseEntity.ok(ResponseDataWrapper.ok(response, "CONTACT_SETTINGS_UPDATED", "Contact settings updated"));
    }
}
