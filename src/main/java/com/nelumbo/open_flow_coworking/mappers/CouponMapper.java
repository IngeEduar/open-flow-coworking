package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.entity.Coupon;
import com.nelumbo.open_flow_coworking.model.response.coupon.CouponResponse;
import com.nelumbo.open_flow_coworking.shared.dto.CouponDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, BranchMapper.class})
public interface CouponMapper {
    CouponDto toDto(Coupon entity);

    CouponResponse toResponse(CouponDto dto);
}
