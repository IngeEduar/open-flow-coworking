package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.model.request.user.UserCreateRequest;
import com.nelumbo.open_flow_coworking.model.request.user.UserUpdateRequest;
import com.nelumbo.open_flow_coworking.model.response.GenericResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.service.UserService;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
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
    public ResponseEntity<UserDetailResponse> userDetail(
            @PathVariable UUID userId
    ) {
        UserDto userDto = userService.userDetail(userId);

        return ResponseEntity.ok(userMapper.toDetailResponse(userDto));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDetailResponse> createOperator(
            @RequestBody UserCreateRequest body
    ) {
        UserDto bodyDto = userMapper.toDto(body);
        UserDto userDto = userService.createOperator(bodyDto);

        return ResponseEntity.ok(userMapper.toDetailResponse(userDto));
    }

    @PatchMapping("/{userId}")
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
    public ResponseEntity<GenericResponse> deleteUser(
            @PathVariable UUID userId
    ) {
        userService.deleteUser(userId);

        GenericResponse response = new GenericResponse(
                "User with id " + userId + " was deleted",
                HttpStatus.OK
        );

        return ResponseEntity.ok(response);
    }
}
