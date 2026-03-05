package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.BranchMapper;
import com.nelumbo.open_flow_coworking.mappers.BranchOperatorMapper;
import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.model.request.branch.BranchCreateRequest;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchListResponse;
import com.nelumbo.open_flow_coworking.model.response.branchOperator.BranchOperatorResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.service.BranchOperatorService;
import com.nelumbo.open_flow_coworking.service.BranchService;
import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import com.nelumbo.open_flow_coworking.shared.dto.BranchOperatorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
@Tag(
        name = "Branches (Sedes) | Sucursales",
        description = "Management of coworking locations | Gestión de ubicaciones de coworking"
)
public class BranchController {

    private final BranchService branchService;
    private final BranchOperatorService branchOperatorService;

    private final BranchMapper branchMapper;
    private final UserMapper userMapper;
    private final BranchOperatorMapper branchOperatorMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List branches | Listar sucursales", description = "Retrieves a paginated list of branches | Recupera una lista paginada de sucursales. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved branches | Sucursales recuperadas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<BranchListResponse>> listBranch(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<BranchDto> branchesDto = branchService.searchBranch(q, limit, page);
        Page<BranchListResponse> response = branchesDto.map(branchMapper::toListResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get branch details | Obtener detalles de la sucursal", description = "Retrieves details of a specific branch | Recupera los detalles de una sucursal específica. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved branch details | Detalles de sucursal recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BranchDetailResponse> branchDetail(
            @PathVariable UUID branchId
    ) {
        BranchDto branchDto = branchService.branchDetail(branchId);
        BranchDetailResponse response = branchMapper.toDetailResponse(branchDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{branchId}/operators")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List branch operators | Listar operadores de la sucursal", description = "Retrieves a paginated list of operators for a specific branch | Recupera una lista paginada de operadores para una sucursal específica. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved operators | Operadores recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<UserListResponse>> listOperators(
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<BranchOperatorDto> users = branchOperatorService.listBranchOperators(branchId, limit, page);
        Page<UserListResponse> response = users.map((branchOperatorDto) ->
                userMapper.toListResponse(branchOperatorDto.user())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create branch | Crear sucursal", description = "Creates a new branch | Crea una nueva sucursal. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Branch successfully created | Sucursal creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid data | Solicitud incorrecta - Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BranchDetailResponse> createBranch(
            @RequestBody BranchCreateRequest body
    ) {
        BranchDto bodyDto = branchMapper.toDto(body);
        BranchDto responseDto = branchService.createBranch(bodyDto);

        return new ResponseEntity<>(branchMapper.toDetailResponse(responseDto), HttpStatus.CREATED);
    }

    @PostMapping("/{branchId}/operators/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add operator to branch | Añadir operador a la sucursal", description = "Assigns an existing operator to a branch | Asigna un operador existente a una sucursal. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operator successfully added | Operador añadido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request | Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BranchOperatorResponse> addOperator(
            @PathVariable UUID branchId,
            @PathVariable UUID userId
    ) {
        BranchOperatorDto branchOperatorDto = branchOperatorService.addOperatorToBranch(userId, branchId);

        return new ResponseEntity<>(branchOperatorMapper.toResponse(branchOperatorDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update branch | Actualizar sucursal", description = "Updates details of an existing branch | Actualiza los detalles de una sucursal existente. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch successfully updated | Sucursal actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request | Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BranchDetailResponse> updateBranch(
            @PathVariable UUID branchId,
            @RequestBody BranchCreateRequest body
    ) {
        BranchDto bodyDto = branchMapper.toDto(body);
        BranchDto responseDto = branchService.updateBranch(branchId, bodyDto);

        return ResponseEntity.ok(branchMapper.toDetailResponse(responseDto));
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete branch | Eliminar sucursal", description = "Deletes an existing branch | Elimina una sucursal existente. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Branch successfully deleted | Sucursal eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteBranch(
            @PathVariable UUID branchId
    ) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{branchId}/operators/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove operator from branch | Eliminar operador de sucursal", description = "Removes an operator from a specific branch | Elimina un operador de una sucursal específica. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operator successfully removed | Operador eliminado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> removeOperator(
            @PathVariable UUID branchId,
            @PathVariable UUID userId
    ) {
        branchOperatorService.deleteOperatorToBranch(userId, branchId);
        return ResponseEntity.noContent().build();
    }
}
