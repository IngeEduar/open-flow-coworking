package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.model.request.authentication.LoginRequest;
import com.nelumbo.open_flow_coworking.model.response.authentication.AuthResponse;
import com.nelumbo.open_flow_coworking.shared.dto.AuthDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthResponse toResponse(AuthDto authDto);
    AuthDto toDto(LoginRequest loginRequest);
}
