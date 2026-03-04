package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.AccessLogMapper;
import com.nelumbo.open_flow_coworking.mappers.ClientMapper;
import com.nelumbo.open_flow_coworking.model.request.client.ClientDocumentRequest;
import com.nelumbo.open_flow_coworking.model.request.client.ClientRequest;
import com.nelumbo.open_flow_coworking.model.response.accessLog.AccessLogResponse;
import com.nelumbo.open_flow_coworking.service.AccessLogService;
import com.nelumbo.open_flow_coworking.shared.dto.AccessLogDto;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/accesses")
@RequiredArgsConstructor
public class AccessController {

    private final AccessLogService accessLogService;

    private final AccessLogMapper accessLogMapper;
    private final ClientMapper clientMapper;

    @GetMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Page<AccessLogResponse>> getAccessByBranch(
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<AccessLogDto> accessLog = accessLogService.findActiveAccessesForBranch(branchId, limit, page);
        Page<AccessLogResponse> response = accessLog.map(accessLogMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<AccessLogResponse> registerEntry(
            @PathVariable UUID branchId,
            @Valid @RequestBody ClientRequest request
    ) {
        ClientDto clientDto = clientMapper.toDto(request);

        AccessLogDto accessLogDto = accessLogService.checkIn(branchId, clientDto);
        AccessLogResponse response = accessLogMapper.toResponse(accessLogDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/checkout")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<AccessLogResponse> registerExit(
            @PathVariable UUID branchId,
            @Valid @RequestBody ClientDocumentRequest request
    ) {
        AccessLogDto accessLogDto = accessLogService.checkOut(branchId, request.document());
        AccessLogResponse response = accessLogMapper.toResponse(accessLogDto);

        return ResponseEntity.ok(response);
    }
}
