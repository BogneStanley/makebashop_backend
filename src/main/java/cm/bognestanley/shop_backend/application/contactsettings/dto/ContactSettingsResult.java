package cm.bognestanley.shop_backend.application.contactsettings.dto;

import java.time.Instant;
import java.util.Map;

public record ContactSettingsResult(Map<String, String> contacts, Instant updatedAt) {
}
