package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.CouponMapper;
import com.nelumbo.open_flow_coworking.model.response.coupon.CouponResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.service.CouponService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupons | Cupones", description = "Manage client loyalty coupons | Gestión de cupones de fidelidad de clientes")
public class CouponController {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PostMapping("/{code}/redeem")
    @Operation(summary = "Redeem a coupon | Redimir un cupón", description = "Marks a specific coupon as redeemed | Marca un cupón específico como redimido. Roles: ADMIN, OPERATOR | Roles: ADMIN, OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coupon successfully redeemed | Cupón redimido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Coupon expired or already redeemed | Solicitud incorrecta - Cupón expirado o ya redimido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Coupon not found | No encontrado - Cupón no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> redeemCoupon(@PathVariable String code) {
        couponService.redeemCoupon(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/branches/{branchId}")
    @Operation(summary = "Get coupons by branch | Obtener cupones por sede", description = "Retrieves a list of coupons assigned to a specific branch | Recupera una lista de cupones asignados a una sede específica. Roles: ADMIN, OPERATOR | Roles: ADMIN, OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved coupons | Cupones recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<CouponResponse>> getCouponsByBranch(@PathVariable UUID branchId) {
        List<CouponResponse> response = couponService.getCouponsByBranch(branchId).stream()
                .map(couponMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @GetMapping("/branches/{branchId}/clients/{document}")
    @Operation(summary = "Get client coupon for branch | Obtener cupón de cliente por sede", description = "Retrieves a specific coupon for a client document and branch | Recupera un cupón específico para un documento de cliente y sede. Roles: ADMIN, OPERATOR | Roles: ADMIN, OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved coupon | Cupón recuperado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Coupon not found | No encontrado - Cupón no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CouponResponse> getCouponByClientAndBranch(
            @PathVariable UUID branchId,
            @PathVariable String document
    ) {
        CouponResponse response = couponMapper.toResponse(couponService.getCouponByClientAndBranch(document, branchId));
        return ResponseEntity.ok(response);
    }
}
