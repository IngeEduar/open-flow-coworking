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
@RequestMapping("/api/accesses")
@RequiredArgsConstructor
public class AdminAccessController {

    private final AccessLogService accessLogService;

    private final AccessLogMapper accessLogMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AccessLogResponse>> getAllAccess(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<AccessLogDto> accessLog = accessLogService.findActiveAccesses(limit, page);
        Page<AccessLogResponse> response = accessLog.map(accessLogMapper::toResponse);

        return ResponseEntity.ok(response);
    }

}
