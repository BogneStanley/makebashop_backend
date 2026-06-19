package cm.bognestanley.shop_backend.infrastructure.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cm.bognestanley.shop_backend.domain.user.entity.User;
import cm.bognestanley.shop_backend.domain.user.entity.UserRole;

public class UserDetailsImpl implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean isActivate;

    public UserDetailsImpl(Long userId, String email, String password, UserRole role, boolean isActivate) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = role == null
                ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        this.isActivate = isActivate;
    }

    public UserDetailsImpl(Long userId, String email, String password, UserRole role) {
        this(userId, email, password, role, true);
    }

    public static UserDetailsImpl from(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isActivate()
        );
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActivate;
    }
}
