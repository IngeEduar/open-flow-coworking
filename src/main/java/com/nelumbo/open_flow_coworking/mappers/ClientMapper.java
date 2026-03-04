package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.entity.Client;
import com.nelumbo.open_flow_coworking.model.request.client.ClientRequest;
import com.nelumbo.open_flow_coworking.model.response.client.ClientResponse;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientDto clientDto);
    ClientDto toDto(Client client);
    ClientDto toDto(ClientRequest clientRequest);
    ClientResponse toResponse(ClientDto clientDto);

}
