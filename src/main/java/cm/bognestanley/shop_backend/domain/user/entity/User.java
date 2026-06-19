package cm.bognestanley.shop_backend.domain.user.entity;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String password;
    private UserRole role;
    private boolean isActivate = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(
            Long id,
            String email,
            String firstName,
            String lastName,
            String avatar,
            String password,
            UserRole role,
            boolean isActivate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.password = password;
        this.role = role;
        this.isActivate = isActivate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createCustomer(
            String email,
            String firstName,
            String lastName,
            String avatar,
            String password) {
        return new User(
                null,
                email,
                firstName,
                lastName,
                avatar,
                password,
                UserRole.CUSTOMER,
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static User createAdmin(
            String email,
            String firstName,
            String lastName,
            String avatar,
            String password) {
        return new User(
                null,
                email,
                firstName,
                lastName,
                avatar,
                password,
                UserRole.ADMIN,
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static User createManager(
            String email,
            String firstName,
            String lastName,
            String avatar,
            String password) {
        return new User(
                null,
                email,
                firstName,
                lastName,
                avatar,
                password,
                UserRole.MANAGER,
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static User create(
            String email,
            String firstName,
            String lastName,
            String avatar,
            String password,
            UserRole role
            ) {
        return new User(
                null,
                email,
                firstName,
                lastName,
                avatar,
                password,
                role,
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateProfile(String email, String firstName, String lastName, String avatar, UserRole role) {
        if (email != null) {
            this.email = email;
        }

        if (firstName != null) {
            this.firstName = firstName;
        }

        if (lastName != null) {
            this.lastName = lastName;
        }

        if (avatar != null) {
            this.avatar = avatar;
        }

        if (role != null) {
            this.role = role;
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActivate = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void desactivate() {
        this.isActivate = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.updatedAt = LocalDateTime.now();
    }
}
