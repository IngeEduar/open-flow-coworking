package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.Coupon;
import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.mappers.CouponMapper;
import com.nelumbo.open_flow_coworking.repository.BranchOperatorRepository;
import com.nelumbo.open_flow_coworking.repository.BranchRepository;
import com.nelumbo.open_flow_coworking.repository.CouponRepository;
import com.nelumbo.open_flow_coworking.service.CouponService;
import com.nelumbo.open_flow_coworking.shared.dto.CouponDto;
import com.nelumbo.open_flow_coworking.shared.enums.CouponStatus;
import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final BranchRepository branchRepository;
    private final BranchOperatorRepository branchOperatorRepository;
    private final CouponMapper couponMapper;

    @Override
    public void redeemCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new OpenFlowException(2, "Coupon", "Code", code));

        validateBranchAccessIfOperator(coupon.getBranch().getId());
        validateStatusAndExpiration(coupon);

        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            throw new OpenFlowException(3000);
        }

        coupon.setStatus(CouponStatus.USED);
        couponRepository.save(coupon);
    }

    @Override
    public CouponDto getCouponByClientAndBranch(String document, UUID branchId) {
        validateBranchAccessIfOperator(branchId);

        Coupon coupon = couponRepository.findByClientDocumentAndBranchId(document, branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Coupon", "Client Document", document));

        validateStatusAndExpiration(coupon);
        return couponMapper.toDto(coupon);
    }

    @Override
    public List<CouponDto> getCouponsByBranch(UUID branchId) {
        validateBranchAccessIfOperator(branchId);

        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        List<Coupon> coupons = couponRepository.findByBranch(branch);

        coupons.forEach(this::validateStatusAndExpiration);

        return coupons.stream()
                .map(couponMapper::toDto)
                .toList();
    }

    private void validateStatusAndExpiration(Coupon coupon) {
        if (coupon.getStatus() == CouponStatus.ACTIVE && coupon.getExpiredAt().isBefore(OffsetDateTime.now())) {
            coupon.setStatus(CouponStatus.EXPIRED);
            couponRepository.save(coupon);
        }
    }

    private void validateBranchAccessIfOperator(UUID branchId) {
        User user = getAuthenticatedUser();
        if (user.getRole() == UserRole.OPERATOR) {
            Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                    .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

            if (!branchOperatorRepository.existsByBranchAndUserAndRecycleFalse(branch, user)) {
                throw new OpenFlowException(2000);
            }
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
            throw new OpenFlowException(1002);
        }

        return (User) authentication.getPrincipal();
    }
}
