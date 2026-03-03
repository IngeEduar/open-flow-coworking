package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends BaseRepository<Branch> {
    @Query("SELECT b FROM Branch b WHERE " +
            "(LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            " LOWER(b.address) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
            "b.recycle = false")
    Page<Branch> searchByQuery(@Param("q") String q, Pageable pageable);

    boolean existsByNameAndAddress(String name, String address);
}
