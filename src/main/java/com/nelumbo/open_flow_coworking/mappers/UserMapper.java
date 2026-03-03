package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.model.request.user.UserCreateRequest;
import com.nelumbo.open_flow_coworking.model.request.user.UserUpdateRequest;
import com.nelumbo.open_flow_coworking.model.response.user.UserDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    UserDto toDto(UserCreateRequest userCreateRequest);
    UserDto toDto(UserUpdateRequest userUpdateRequest);
    UserListResponse toListResponse(UserDto userDto);
    UserDetailResponse toDetailResponse(UserDto userDto);
}
