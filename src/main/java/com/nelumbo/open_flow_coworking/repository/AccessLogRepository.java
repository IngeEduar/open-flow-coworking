package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.AccessLog;
import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.Client;
import com.nelumbo.open_flow_coworking.shared.dto.TopClientProjection;
import com.nelumbo.open_flow_coworking.shared.dto.TopRevenueProjection;
import com.nelumbo.open_flow_coworking.shared.enums.AccessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessLogRepository extends BaseRepository<AccessLog> {
    Page<AccessLog> findByBranchAndStatus(Branch branch, AccessStatus status, Pageable pageable);
    Page<AccessLog> findByStatus(AccessStatus status, Pageable pageable);
    Optional<AccessLog> findByClientAndBranchAndStatus(Client client, Branch branch, AccessStatus status);

    int countByBranchAndStatus(Branch branch, AccessStatus status);
    boolean existsByClientAndStatus(Client client, AccessStatus status);

    @Query(value = "SELECT COALESCE(SUM(EXTRACT(EPOCH FROM (a.check_out - a.check_in))) / 3600.0, 0) FROM access_log a WHERE a.client_id = :client_id AND a.branch_id = :branch_id AND a.status = 'COMPLETED'", nativeQuery = true)
    Double sumDurationByClientAndBranch(@Param("client_id") UUID clientId, @Param("branch_id") UUID branchId);

    @Query("SELECT a.client.document AS document, COUNT(a.id) AS totalAccesses FROM AccessLog a GROUP BY a.client.document ORDER BY totalAccesses DESC")
    List<TopClientProjection> findTopClients(Pageable pageable);

    @Query("SELECT a.client.document AS document, COUNT(a.id) AS totalAccesses FROM AccessLog a WHERE a.branch.id = :branchId GROUP BY a.client.document ORDER BY totalAccesses DESC")
    List<TopClientProjection> findTopClientsByBranch(@Param("branchId") UUID branchId, Pageable pageable);

    @Query("SELECT a.client FROM AccessLog a GROUP BY a.client HAVING COUNT(a.id) = 1")
    List<Client> findFirstTimeClients();

    @Query("SELECT COALESCE(SUM(a.price), 0) FROM AccessLog a WHERE a.branch.id = :branchId AND a.checkOut >= :startDate AND a.status = 'COMPLETED'")
    BigDecimal sumRevenueByBranchAndDateAfter(@Param("branchId") UUID branchId,
            @Param("startDate") OffsetDateTime startDate);

    @Query("SELECT a.operator.email AS name, SUM(a.price) AS totalRevenue FROM AccessLog a WHERE a.checkOut >= :startDate AND a.status = 'COMPLETED' GROUP BY a.operator.email ORDER BY totalRevenue DESC")
    List<TopRevenueProjection> findTopOperatorsByRevenue(@Param("startDate") OffsetDateTime startDate,
            Pageable pageable);

    @Query("SELECT a.branch.name AS name, SUM(a.price) AS totalRevenue FROM AccessLog a WHERE a.checkOut >= :startDate AND a.status = 'COMPLETED' GROUP BY a.branch.name ORDER BY totalRevenue DESC")
    List<TopRevenueProjection> findTopBranchesByRevenue(@Param("startDate") OffsetDateTime startDate,
            Pageable pageable);
}
