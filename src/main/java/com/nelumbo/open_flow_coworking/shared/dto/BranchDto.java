package com.nelumbo.open_flow_coworking.shared.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private String name;
    private String address;
    private int maxCapacity;
    private BigDecimal hourlyRate;
}
