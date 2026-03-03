package com.nelumbo.open_flow_coworking.shared.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private String document;
    private String email;
    private String name;
}
