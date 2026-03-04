package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.Client;
import com.nelumbo.open_flow_coworking.entity.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends BaseRepository<Coupon> {
    List<Coupon> findByBranch(Branch branch);

    Optional<Coupon> findByCode(String code);
    Optional<Coupon> findByClientDocumentAndBranchId(String document, UUID branchId);

    boolean existsByClientAndBranch(Client client, Branch branch);
}
