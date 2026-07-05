package cm.bognestanley.shop_backend.domain.contactsettings.entity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ContactSettings {

    public static final Long SINGLETON_ID = 1L;

    private final Long id;
    private final Map<String, String> contacts;
    private Instant updatedAt;

    public ContactSettings(Long id, Map<String, String> contacts, Instant updatedAt) {
        this.id = id;
        this.contacts = new HashMap<>(contacts);
        this.updatedAt = updatedAt;
    }

    public static ContactSettings createEmpty() {
        return new ContactSettings(SINGLETON_ID, new HashMap<>(), Instant.now());
    }

    public void mergeContacts(Map<String, String> updates) {
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            String key = normalizeKey(entry.getKey());
            String value = entry.getValue();
            if (value == null || value.isBlank()) {
                contacts.remove(key);
            } else {
                contacts.put(key, value.trim());
            }
        }
        updatedAt = Instant.now();
    }

    private String normalizeKey(String key) {
        return key.trim().toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public Map<String, String> getContacts() {
        return Map.copyOf(contacts);
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
