package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.BranchMapper;
import com.nelumbo.open_flow_coworking.model.request.branch.BranchCreateRequest;
import com.nelumbo.open_flow_coworking.model.response.GenericResponse;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchListResponse;
import com.nelumbo.open_flow_coworking.service.BranchService;
import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branch")
public class BranchController {

    private final BranchService branchService;
    private final BranchMapper branchMapper;

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDetailResponse> createBranch(
            @RequestBody BranchCreateRequest body
    ) {
        BranchDto bodyDto = branchMapper.toDto(body);
        BranchDto responseDto = branchService.createBranch(bodyDto);

        return ResponseEntity.ok(branchMapper.toDetailResponse(responseDto));
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
    public ResponseEntity<GenericResponse> deleteBranch(
            @PathVariable UUID branchId
    ) {
        branchService.deleteBranch(branchId);

        GenericResponse response = new GenericResponse(
                "Branch with id " + branchId + " was deleted",
                HttpStatus.OK
        );

        return ResponseEntity.ok(response);
    }
}
