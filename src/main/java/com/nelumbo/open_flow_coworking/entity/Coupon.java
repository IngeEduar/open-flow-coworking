package com.nelumbo.open_flow_coworking.entity;

import com.nelumbo.open_flow_coworking.shared.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "code", nullable = false, unique = true, updatable = false, length = 100)
    private String code;

    @Column(name = "expired_at", updatable = false)
    private OffsetDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CouponStatus status;
}
