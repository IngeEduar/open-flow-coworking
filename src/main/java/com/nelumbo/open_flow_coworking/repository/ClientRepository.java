package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends BaseRepository<Client> {
    Optional<Client> findByDocument(String document);
}
