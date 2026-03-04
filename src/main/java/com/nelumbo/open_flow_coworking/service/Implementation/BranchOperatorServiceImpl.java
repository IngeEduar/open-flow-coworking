package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.BranchOperator;
import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.mappers.BranchOperatorMapper;
import com.nelumbo.open_flow_coworking.repository.BranchOperatorRepository;
import com.nelumbo.open_flow_coworking.repository.BranchRepository;
import com.nelumbo.open_flow_coworking.repository.UserRepository;
import com.nelumbo.open_flow_coworking.security.jwt.JwtService;
import com.nelumbo.open_flow_coworking.service.BranchOperatorService;
import com.nelumbo.open_flow_coworking.shared.dto.BranchOperatorDto;
import com.nelumbo.open_flow_coworking.util.CreatePageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchOperatorServiceImpl implements BranchOperatorService {

    private final BranchOperatorMapper branchOperatorMapper;

    private final BranchOperatorRepository branchOperatorRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public Page<BranchOperatorDto> listBranchOperators(UUID branchId, int limit, int page) {
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        Pageable pageable = CreatePageable.buildPageable(page, limit, "createdAt");
        Page<BranchOperator> branchOperators = branchOperatorRepository.findByBranchAndRecycleFalse(branch, pageable);

        return branchOperators.map(branchOperatorMapper::toDto);
    }

    @Override
    public Page<BranchOperatorDto> listOperatorBranches(String token, int limit, int page) {
        String userEmail = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new OpenFlowException(2, "User", "email", userEmail));

        Pageable pageable = CreatePageable.buildPageable(page, limit, "createdAt");
        Page<BranchOperator> branchOperators = branchOperatorRepository.findByUserAndRecycleFalse(user, pageable);

        return branchOperators.map(branchOperatorMapper::toDto);
    }

    @Override
    public BranchOperatorDto addOperatorToBranch(UUID userId, UUID branchId) {
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        User user = userRepository.findByIdAndRecycleFalse(userId)
                .orElseThrow(() -> new OpenFlowException(2, "User", "ID", userId.toString()));

        if (branchOperatorRepository.existsByBranchAndUserAndRecycleFalse(branch, user)) {
            throw new OpenFlowException(3, "Operator", "branch", branchId);
        }

        BranchOperator branchOperator = BranchOperator
                .builder()
                .branch(branch)
                .user(user)
                .build();

        branchOperator = branchOperatorRepository.save(branchOperator);

        return branchOperatorMapper.toDto(branchOperator);
    }

    @Override
    public void deleteOperatorToBranch(UUID userId, UUID branchId) {
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        User user = userRepository.findByIdAndRecycleFalse(userId)
                .orElseThrow(() -> new OpenFlowException(2, "User", "ID", userId.toString()));

        BranchOperator branchOperator = branchOperatorRepository.findByBranchAndUserAndRecycleFalse(branch, user);

        if (branchOperator == null) {
            throw new OpenFlowException(3, "Operator", "branch", branchId);
        }

        branchOperator.setRecycle(true);
        branchOperatorRepository.save(branchOperator);
    }
}
