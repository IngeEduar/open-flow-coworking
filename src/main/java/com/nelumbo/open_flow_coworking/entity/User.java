package com.nelumbo.open_flow_coworking.entity;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "backoffice_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

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
}
