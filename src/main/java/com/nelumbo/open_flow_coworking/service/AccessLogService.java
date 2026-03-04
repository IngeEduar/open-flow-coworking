package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.shared.dto.AccessLogDto;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AccessLogService {
    Page<AccessLogDto> findActiveAccessesForBranch(UUID branchId, int limit, int page);
    Page<AccessLogDto> findActiveAccesses(int limit, int page);
    AccessLogDto checkIn(UUID branchId, ClientDto clientDto);
    AccessLogDto checkOut(UUID branchId, String clientDocument);
}
