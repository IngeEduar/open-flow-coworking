package com.nelumbo.open_flow_coworking.shared.dto;

import com.nelumbo.open_flow_coworking.shared.enums.CouponStatus;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDto {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private BranchDto branch;
    private ClientDto client;
    private OffsetDateTime expiredAt;
    private CouponStatus status;
}
