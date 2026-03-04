package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.shared.dto.CouponDto;

import java.util.List;
import java.util.UUID;

public interface CouponService {
    void redeemCoupon(String code);
    CouponDto getCouponByClientAndBranch(String document, UUID branchId);
    List<CouponDto> getCouponsByBranch(UUID branchId);
}
