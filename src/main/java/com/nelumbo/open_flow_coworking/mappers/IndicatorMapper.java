package com.nelumbo.open_flow_coworking.mappers;


import com.nelumbo.open_flow_coworking.model.response.indicator.RevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopBranchRevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopClientResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopOperatorRevenueResponse;
import com.nelumbo.open_flow_coworking.shared.dto.RevenueDto;
import com.nelumbo.open_flow_coworking.shared.dto.TopClientProjection;
import com.nelumbo.open_flow_coworking.shared.dto.TopRevenueProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndicatorMapper {
    TopClientResponse toTopClientResponse(TopClientProjection projection);
    RevenueResponse toRevenueResponse(RevenueDto dto);
    TopOperatorRevenueResponse toTopOperatorRevenueResponse(TopRevenueProjection projection);
    TopBranchRevenueResponse toTopBranchRevenueResponse(TopRevenueProjection projection);
}
