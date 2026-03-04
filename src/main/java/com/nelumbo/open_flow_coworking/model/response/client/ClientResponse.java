package com.nelumbo.open_flow_coworking.model.response.client;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse(
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    String document,
    String email
) {
}
