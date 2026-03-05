package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.AccessLogMapper;
import com.nelumbo.open_flow_coworking.mappers.ClientMapper;
import com.nelumbo.open_flow_coworking.model.request.client.ClientDocumentRequest;
import com.nelumbo.open_flow_coworking.model.request.client.ClientRequest;
import com.nelumbo.open_flow_coworking.model.response.accessLog.AccessLogResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.service.AccessLogService;
import com.nelumbo.open_flow_coworking.shared.dto.AccessLogDto;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.UUID;

@RestController
@RequestMapping("/api/branches/{branchId}/accesses")
@RequiredArgsConstructor
@Tag(
        name = "Access Management | Gestión de Accesos",
        description = "Check-in and check-out of clients in branches | Ingreso y salida de clientes en sedes"
)
public class AccessController {

    private final AccessLogService accessLogService;

    private final AccessLogMapper accessLogMapper;
    private final ClientMapper clientMapper;

    @GetMapping
    @PreAuthorize("hasRole('OPERATOR')")
    @Operation(summary = "Get branch access logs | Obtener registros de acceso de la sede", description = "Retrieves a paginated list of active access logs for a specific branch | Recupera una lista paginada de registros de acceso activos para una sede específica. Role: OPERATOR | Rol: OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved access logs | Registros de acceso recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token | No autorizado - Token inválido o faltante", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires OPERATOR role | Prohibido - Requiere rol OPERADOR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Register check-in | Registrar ingreso", description = "Registers a client check-in at a specific branch, validating capacity | Registra el ingreso de un cliente en una sede específica, validando la capacidad. Role: OPERATOR | Rol: OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully registered check-in | Ingreso registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input, branch at full capacity, or active access exists | Solicitud incorrecta - Entrada inválida, sede en capacidad máxima o acceso activo existente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Branch not found | No encontrado - Sede no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "Register check-out | Registrar salida", description = "Registers a client check-out, calculates bill, and frees branch capacity | Registra la salida de un cliente, calcula la factura y libera cupo en la sede. Role: OPERATOR | Rol: OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully registered check-out | Salida registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or no active access found | Solicitud incorrecta - Entrada inválida o acceso activo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Branch not found | No encontrado - Sede no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AccessLogResponse> registerExit(
            @PathVariable UUID branchId,
            @Valid @RequestBody ClientDocumentRequest request
    ) {
        AccessLogDto accessLogDto = accessLogService.checkOut(branchId, request.document());
        AccessLogResponse response = accessLogMapper.toResponse(accessLogDto);

        return ResponseEntity.ok(response);
    }
}
