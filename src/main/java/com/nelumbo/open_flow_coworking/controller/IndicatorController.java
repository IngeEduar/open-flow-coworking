package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.ClientMapper;
import com.nelumbo.open_flow_coworking.service.IndicatorService;
import com.nelumbo.open_flow_coworking.mappers.IndicatorMapper;
import com.nelumbo.open_flow_coworking.model.response.client.ClientResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.RevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopBranchRevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopClientResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopOperatorRevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/indicators")
@RequiredArgsConstructor
@Tag(name = "Indicators | Indicadores", description = "Analytics and metrics | Analíticas y métricas")
public class IndicatorController {

    private final IndicatorService indicatorService;
    private final IndicatorMapper indicatorMapper;

    private final ClientMapper clientMapper;

    @GetMapping("/clients/top-accesses")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Get top clients global | Obtener top clientes global", description = "Retrieves top 10 clients with the most accesses across all branches | Recupera el top 10 de clientes con más accesos en todas las sedes. Roles: ADMIN, OPERATOR | Roles: ADMIN, OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved top clients | Top clientes recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TopClientResponse>> getTopClientsGlobal() {
        return ResponseEntity.ok(indicatorService.getTopClientsGlobal().stream()
                .map(indicatorMapper::toTopClientResponse).toList());
    }

    @GetMapping("/branches/{branchId}/clients/top-accesses")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Get top clients by branch | Obtener top clientes por sede", description = "Retrieves top 10 clients with the most accesses in a specific branch | Recupera el top 10 de clientes con más accesos en una sede específica. Roles: ADMIN, OPERATOR | Roles: ADMIN, OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved top clients | Top clientes recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TopClientResponse>> getTopClientsByBranch(@PathVariable UUID branchId) {
        return ResponseEntity.ok(indicatorService.getTopClientsByBranch(branchId).stream()
                .map(indicatorMapper::toTopClientResponse).toList());
    }

    @GetMapping("/clients/first-time")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Get first time clients | Obtener clientes de primera vez", description = "Retrieves a list of clients entering for the first time | Recupera una lista de clientes que ingresan por primera vez. Roles: ADMIN, OPERATOR | Roles: ADMIN, OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clients | Clientes recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ClientResponse>> getFirstTimeClients() {
        return ResponseEntity.ok(indicatorService.getFirstTimeClients().stream()
                .map(clientMapper::toResponse).toList());
    }

    @GetMapping("/branches/{branchId}/revenue")
    @PreAuthorize("hasRole('OPERATOR')")
    @Operation(summary = "Get branch revenue | Obtener ingresos de la sede", description = "Retrieves revenue metrics (today, week, month, year) for a specific branch | Recupera las métricas de ingresos (hoy, semana, mes, año) para una sede específica. Role: OPERATOR | Rol: OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved revenue | Ingresos recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RevenueResponse> getBranchRevenue(@PathVariable UUID branchId) {
        return ResponseEntity.ok(indicatorMapper.toRevenueResponse(indicatorService.getBranchRevenue(branchId)));
    }

    @GetMapping("/operators/top-revenue/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get top operators weekly revenue | Obtener ingresos semanales de top operadores", description = "Retrieves top 3 operators with the most revenue this week | Recupera el top 3 de operadores con más ingresos esta semana. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved top operators | Top operadores recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TopOperatorRevenueResponse>> getTopOperatorsWeekly() {
        return ResponseEntity.ok(indicatorService.getTopOperatorsWeekly().stream()
                .map(indicatorMapper::toTopOperatorRevenueResponse).toList());
    }

    @GetMapping("/branches/top-revenue/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get top branches weekly revenue | Obtener ingresos semanales de top sedes", description = "Retrieves top 3 branches with the most revenue this week | Recupera el top 3 de sedes con más ingresos esta semana. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved top branches | Top sedes recuperadas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TopBranchRevenueResponse>> getTopBranchesWeekly() {
        return ResponseEntity.ok(indicatorService.getTopBranchesWeekly().stream()
                .map(indicatorMapper::toTopBranchRevenueResponse).toList());
    }
}
