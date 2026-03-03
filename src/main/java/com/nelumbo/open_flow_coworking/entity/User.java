package com.nelumbo.open_flow_coworking.entity;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "backoffice_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "document", nullable = false, length = 20, unique = true, updatable = false)
    private String document;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return !this.getRecycle(); }
}