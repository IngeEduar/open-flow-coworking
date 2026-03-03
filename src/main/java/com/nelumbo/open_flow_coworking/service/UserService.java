package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    Page<UserDto> searchUsers(String q, int limit, int page);
    UserDto userDetail(UUID userId);
    UserDto createOperator(UserDto userDto);
    UserDto updateUser(UUID userId, UserDto userDto);
    void deleteUser(UUID userId);
}
