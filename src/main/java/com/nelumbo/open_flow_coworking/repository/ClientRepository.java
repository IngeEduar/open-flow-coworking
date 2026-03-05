package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.Client;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends BaseRepository<Client> {
    Optional<Client> findByDocument(String document);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Client c WHERE c.document = :document")
    Optional<Client> findByDocumentForUpdate(@Param("document") String document);
}
