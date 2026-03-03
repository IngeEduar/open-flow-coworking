package com.nelumbo.open_flow_coworking.model.request.user;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String name;
    private String email;
    private String document;
    private UserRole role;
    private String password;
}
