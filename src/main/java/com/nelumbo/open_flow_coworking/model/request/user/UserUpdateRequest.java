package com.nelumbo.open_flow_coworking.model.request.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String name;
    private String email;
    private String password;
}
