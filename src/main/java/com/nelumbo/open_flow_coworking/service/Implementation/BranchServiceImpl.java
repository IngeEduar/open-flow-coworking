package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.mappers.BranchMapper;
import com.nelumbo.open_flow_coworking.repository.BranchRepository;
import com.nelumbo.open_flow_coworking.service.BranchService;
import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import com.nelumbo.open_flow_coworking.util.CreatePageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    public Page<BranchDto> searchBranch(String q, int limit, int page) {
        Pageable pageable = CreatePageable.buildPageable(page, limit, "createdAt");

        Page<Branch> branches;

        if (q == null) {
            branches = branchRepository.findAllByRecycleFalse(pageable);
        } else {
            branches = branchRepository.searchByQuery(q, pageable);
        }

        return branches.map(branchMapper::toDto);
    }

    @Override
    public BranchDto branchDetail(UUID branchId) {
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        return branchMapper.toDto(branch);
    }

    @Override
    public BranchDto createBranch(BranchDto branchDto) {
        if (branchRepository.existsByNameAndAddress(branchDto.getName(), branchDto.getAddress())) {
            throw new OpenFlowException(
                    3,
                    "Branch",
                    "Name and Address",
                    branchDto.getName()
            );
        }

        Branch branch = branchMapper.toEntity(branchDto);
        branch = branchRepository.save(branch);

        return branchMapper.toDto(branch);
    }

    @Override
    public BranchDto updateBranch(UUID branchId, BranchDto branchDto) {
        if (
                branchDto.getName() != null &&
                        branchDto.getAddress() != null
        ) {
            if (branchRepository.existsByNameAndAddress(branchDto.getName(), branchDto.getAddress())) {
                throw new OpenFlowException(
                        3,
                        "Branch",
                        "Name and Address",
                        branchDto.getName()
                );
            }
        }

        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        if (
                branchDto.getName() != null &&
                        !branchDto.getName().equals(branch.getName())
        ) {
            branch.setName(branchDto.getName());
        }

        if (
                branchDto.getAddress() != null &&
                        !branchDto.getAddress().equals(branch.getAddress())
        ) {
            branch.setAddress(branchDto.getAddress());
        }

        if (
                branchDto.getMaxCapacity() != 0 &&
                        branchDto.getMaxCapacity() != branch.getMaxCapacity()
        ) {
            branch.setMaxCapacity(branchDto.getMaxCapacity());
        }

        if (
                branchDto.getHourlyRate() != null &&
                        !branchDto.getHourlyRate().equals(branch.getHourlyRate())
        ) {
            branch.setHourlyRate(branchDto.getHourlyRate());
        }

        branch = branchRepository.save(branch);

        return branchMapper.toDto(branch);
    }

    @Override
    public void deleteBranch(UUID branchId) {
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        branch.setRecycle(false);
        branchRepository.save(branch);
    }
}
