package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.BranchOperator;
import com.nelumbo.open_flow_coworking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchOperatorRepository extends BaseRepository<BranchOperator> {
    Page<BranchOperator> findByBranchAndRecycleFalse(Branch branch, Pageable pageable);
    Page<BranchOperator> findByUserAndRecycleFalse(User user, Pageable pageable);

    BranchOperator findByBranchAndUserAndRecycleFalse(Branch branch, User user);

    boolean existsByBranchAndUserAndRecycleFalse(Branch branch, User user);
}
