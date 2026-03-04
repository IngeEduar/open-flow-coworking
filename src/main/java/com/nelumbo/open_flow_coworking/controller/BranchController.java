package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.BranchMapper;
import com.nelumbo.open_flow_coworking.mappers.BranchOperatorMapper;
import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.model.request.branch.BranchCreateRequest;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchListResponse;
import com.nelumbo.open_flow_coworking.model.response.branchOperator.BranchOperatorResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.service.BranchOperatorService;
import com.nelumbo.open_flow_coworking.service.BranchService;
import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import com.nelumbo.open_flow_coworking.shared.dto.BranchOperatorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;
    private final BranchOperatorService branchOperatorService;

    private final BranchMapper branchMapper;
    private final UserMapper userMapper;
    private final BranchOperatorMapper branchOperatorMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BranchListResponse>> listBranch(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<BranchDto> branchesDto = branchService.searchBranch(q, limit, page);
        Page<BranchListResponse> response = branchesDto.map(branchMapper::toListResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDetailResponse> branchDetail(
            @PathVariable UUID branchId
    ) {
        BranchDto branchDto = branchService.branchDetail(branchId);
        BranchDetailResponse response = branchMapper.toDetailResponse(branchDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{branchId}/operators")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserListResponse>> listOperators(
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<BranchOperatorDto> users = branchOperatorService.listBranchOperators(branchId, limit, page);
        Page<UserListResponse> response = users.map((branchOperatorDto) ->
                userMapper.toListResponse(branchOperatorDto.user())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDetailResponse> createBranch(
            @RequestBody BranchCreateRequest body
    ) {
        BranchDto bodyDto = branchMapper.toDto(body);
        BranchDto responseDto = branchService.createBranch(bodyDto);

        return new ResponseEntity<>(branchMapper.toDetailResponse(responseDto), HttpStatus.CREATED);
    }

    @PostMapping("/{branchId}/operators/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchOperatorResponse> addOperator(
            @PathVariable UUID branchId,
            @PathVariable UUID userId
    ) {
        BranchOperatorDto branchOperatorDto = branchOperatorService.addOperatorToBranch(userId, branchId);

        return new ResponseEntity<>(branchOperatorMapper.toResponse(branchOperatorDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDetailResponse> updateBranch(
            @PathVariable UUID branchId,
            @RequestBody BranchCreateRequest body
    ) {
        BranchDto bodyDto = branchMapper.toDto(body);
        BranchDto responseDto = branchService.updateBranch(branchId, bodyDto);

        return ResponseEntity.ok(branchMapper.toDetailResponse(responseDto));
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBranch(
            @PathVariable UUID branchId
    ) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{branchId}/operators/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeOperator(
            @PathVariable UUID branchId,
            @PathVariable UUID userId
    ) {
        branchOperatorService.deleteOperatorToBranch(userId, branchId);
        return ResponseEntity.noContent().build();
    }
}
