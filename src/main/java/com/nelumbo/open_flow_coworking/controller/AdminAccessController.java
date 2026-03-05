package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.AccessLogMapper;
import com.nelumbo.open_flow_coworking.model.response.accessLog.AccessLogResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.service.AccessLogService;
import com.nelumbo.open_flow_coworking.shared.dto.AccessLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/accesses")
@RequiredArgsConstructor
@Tag(
        name = "Admin Access Logs | Registros de Acceso de Administrador",
        description = "Global view of access logs for administrators | Vista global de registros de acceso para administradores"
)
public class AdminAccessController {

    private final AccessLogService accessLogService;

    private final AccessLogMapper accessLogMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all access logs | Obtener todos los registros de acceso", description = "Retrieves a paginated list of all access logs across all branches | Recupera una lista paginada de todos los registros de acceso en todas las sedes. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved access logs | Registros de acceso recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token | No autorizado - Token inválido o faltante", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role | Prohibido - Requiere rol ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<AccessLogResponse>> getAllAccess(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<AccessLogDto> accessLog = accessLogService.findActiveAccesses(limit, page);
        Page<AccessLogResponse> response = accessLog.map(accessLogMapper::toResponse);

        return ResponseEntity.ok(response);
    }

}
