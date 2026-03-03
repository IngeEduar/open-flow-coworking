package com.nelumbo.open_flow_coworking.shared.dto;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private String name;
    private String email;
    private String document;
    private String password;
    private String salt;
    private UserRole role;
}
