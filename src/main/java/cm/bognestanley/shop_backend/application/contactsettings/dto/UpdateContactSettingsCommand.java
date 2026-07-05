package cm.bognestanley.shop_backend.application.contactsettings.dto;

import java.util.Map;

public record UpdateContactSettingsCommand(Map<String, String> contacts) {
}
