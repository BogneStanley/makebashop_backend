package cm.bognestanley.shop_backend.presentation.dto.response.contactsettings;

import java.time.Instant;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Configuration des contacts de la boutique")
public record ContactSettingsResponse(
        @Schema(description = "Contacts indexés par clé (ex: instagram, email, whatsapp_group)")
        Map<String, String> contacts,
        Instant updatedAt) {
}
