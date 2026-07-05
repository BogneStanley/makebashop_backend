package cm.bognestanley.shop_backend.presentation.dto.request.contactsettings;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Mise à jour partielle des contacts. Clés connues : instagram, facebook, tiktok, email, phone, address, whatsapp, whatsapp_group. Toute autre clé est acceptée. Valeur vide ou null supprime l'entrée.")
public record UpdateContactSettingsRequest(
        @NotNull @NotEmpty Map<String, String> contacts) {
}
