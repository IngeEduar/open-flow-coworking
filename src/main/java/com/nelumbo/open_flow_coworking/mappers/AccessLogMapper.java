package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.entity.AccessLog;
import com.nelumbo.open_flow_coworking.model.response.accessLog.AccessLogResponse;
import com.nelumbo.open_flow_coworking.shared.dto.AccessLogDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                ClientMapper.class,
                BranchMapper.class,
                UserMapper.class,
        }
)
public interface AccessLogMapper {
    AccessLog toEntity(AccessLogDto accessLogDto);
    AccessLogDto toDto(AccessLog accessLog);
    AccessLogResponse toResponse(AccessLogDto accessLogDto);
}
