package com.nelumbo.open_flow_coworking.shared.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientDto (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    String document,
    String email,
    String name
) {
}
