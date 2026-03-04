package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.AccessLog;
import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.Client;
import com.nelumbo.open_flow_coworking.shared.enums.AccessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessLogRepository extends BaseRepository<AccessLog> {
    Page<AccessLog> findByBranchAndStatus(Branch branch, AccessStatus status, Pageable pageable);
    Optional<AccessLog> findByClientAndBranchAndStatus(Client client, Branch branch, AccessStatus status);

    int countByBranchAndStatus(Branch branch, AccessStatus status);

    boolean existsByClientAndStatus(Client client, AccessStatus status);
}
