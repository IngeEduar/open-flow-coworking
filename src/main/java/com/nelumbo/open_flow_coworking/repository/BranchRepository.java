package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.Branch;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends BaseRepository<Branch> {
    @Query("SELECT b FROM Branch b WHERE " +
            "(LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            " LOWER(b.address) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
            "b.recycle = false")
    Page<Branch> searchByQuery(@Param("q") String q, Pageable pageable);

    boolean existsByNameAndAddress(String name, String address);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Branch b WHERE b.id = :id AND b.recycle = false")
    Optional<Branch> findByIdAndRecycleFalseForUpdate(@Param("id") UUID id);
}
