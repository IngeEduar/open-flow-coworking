package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.CouponMapper;
import com.nelumbo.open_flow_coworking.model.response.coupon.CouponResponse;
import com.nelumbo.open_flow_coworking.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PostMapping("/{code}/redeem")
    public ResponseEntity<Void> redeemCoupon(@PathVariable String code) {
        couponService.redeemCoupon(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/branches/{branchId}")
    public ResponseEntity<List<CouponResponse>> getCouponsByBranch(@PathVariable UUID branchId) {
        List<CouponResponse> response = couponService.getCouponsByBranch(branchId).stream()
                .map(couponMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/branches/{branchId}/clients/{document}")
    public ResponseEntity<CouponResponse> getCouponByClientAndBranch(
            @PathVariable UUID branchId,
            @PathVariable String document) {
        CouponResponse response = couponMapper.toResponse(couponService.getCouponByClientAndBranch(document, branchId));
        return ResponseEntity.ok(response);
    }
}
