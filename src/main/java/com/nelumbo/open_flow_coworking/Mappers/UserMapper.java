package com.nelumbo.open_flow_coworking.Mappers;

import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
