package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.entity.BranchOperator;
import com.nelumbo.open_flow_coworking.model.response.branchOperator.BranchOperatorResponse;
import com.nelumbo.open_flow_coworking.shared.dto.BranchOperatorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BranchMapper.class, UserMapper.class})
public interface BranchOperatorMapper {
    BranchOperatorDto toDto(BranchOperator branchOperator);
    BranchOperatorResponse toResponse(BranchOperatorDto branchOperatorDto);
}
