package com.nelumbo.open_flow_coworking.model.response.user;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private String name;
    private String email;
    private String document;
    private UserRole role;
}
