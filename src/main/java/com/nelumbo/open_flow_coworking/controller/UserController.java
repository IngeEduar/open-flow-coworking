package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.BranchMapper;
import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.model.request.user.UserCreateRequest;
import com.nelumbo.open_flow_coworking.model.request.user.UserUpdateRequest;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchListResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.service.AuthService;
import com.nelumbo.open_flow_coworking.service.BranchOperatorService;
import com.nelumbo.open_flow_coworking.service.UserService;
import com.nelumbo.open_flow_coworking.shared.dto.BranchOperatorDto;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
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
@RequestMapping("/api/users")
@Tag(
        name = "Users | Usuarios",
        description = "Operations for user management | Operaciones para la gestión de usuarios"
)
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final BranchOperatorService branchOperatorService;

    private final UserMapper userMapper;
    private final BranchMapper branchMapper;

    @GetMapping
    @Operation(summary = "List users | Listar usuarios", description = "Retrieves a paginated list of users | Recupera una lista paginada de usuarios", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users | Usuarios recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<UserListResponse>> listUsers(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<UserDto> usersDto = userService.searchUsers(q, limit, page);
        Page<UserListResponse> response = usersDto.map(userMapper::toListResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user details | Obtener detalles del usuario", description = "Retrieves details of a specific user | Recupera los detalles de un usuario específico", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user details | Detalles del usuario recuperados exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserDetailResponse> userDetail(
            @PathVariable UUID userId
    ) {
        UserDto userDto = userService.userDetail(userId);

        return ResponseEntity.ok(userMapper.toDetailResponse(userDto));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile | Obtener perfil del usuario actual", description = "Retrieves the profile of the currently authenticated user | Recupera el perfil del usuario autenticado actualmente. Roles: ALL | Roles: TODOS", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved profile | Perfil recuperado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserDetailResponse> getProfile() {
        UserDto userDto = authService.getProfile();

        return ResponseEntity.ok(userMapper.toDetailResponse(userDto));
    }

    @GetMapping("/me/branches")
    @PreAuthorize("hasRole('OPERATOR')")
    @Operation(summary = "Get current operator branches | Obtener sucursales del operador actual", description = "Retrieves branches assigned to the current operator | Recupera las sucursales asignadas al operador actual. Role: OPERATOR | Rol: OPERADOR", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved branches | Sucursales recuperadas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<BranchListResponse>> getMyBranches(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<BranchOperatorDto> branches = branchOperatorService.listOperatorBranches(token, limit, page);
        Page<BranchListResponse> response = branches.map((branchOperatorDto) ->
                branchMapper.toListResponse(branchOperatorDto.branch())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create operator | Crear operador", description = "Creates a new operator user | Crea un nuevo usuario operador. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operator successfully created | Operador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request | Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserDetailResponse> createOperator(
            @RequestBody UserCreateRequest body
    ) {
        UserDto bodyDto = userMapper.toDto(body);
        UserDto userDto = userService.createOperator(bodyDto);

        return new ResponseEntity<>(userMapper.toDetailResponse(userDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Update user | Actualizar usuario", description = "Updates details of an existing user | Actualiza los detalles de un usuario existente", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully updated | Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Bad Request | Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserDetailResponse> updateUser(
            @PathVariable UUID userId,
            @RequestBody UserUpdateRequest body
    ) {
        UserDto bodyDto = userMapper.toDto(body);
        UserDto userDto = userService.updateUser(userId, bodyDto);

        return ResponseEntity.ok(userMapper.toDetailResponse(userDto));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user | Eliminar usuario", description = "Deletes an existing user | Elimina un usuario existente. Role: ADMIN | Rol: ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted | Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized | No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden | Prohibido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found | No encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID userId
    ) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
