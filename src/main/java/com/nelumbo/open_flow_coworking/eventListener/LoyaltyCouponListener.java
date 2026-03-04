package com.nelumbo.open_flow_coworking.eventListener;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.Client;
import com.nelumbo.open_flow_coworking.entity.Coupon;
import com.nelumbo.open_flow_coworking.model.event.CheckoutCompletedEvent;
import com.nelumbo.open_flow_coworking.model.request.notification.NotificationRequest;
import com.nelumbo.open_flow_coworking.repository.AccessLogRepository;
import com.nelumbo.open_flow_coworking.repository.BranchRepository;
import com.nelumbo.open_flow_coworking.repository.ClientRepository;
import com.nelumbo.open_flow_coworking.repository.CouponRepository;
import com.nelumbo.open_flow_coworking.service.NotificationService;
import com.nelumbo.open_flow_coworking.shared.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoyaltyCouponListener {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyCouponListener.class);
    private static final int REQUIRED_HOURS_FOR_COUPON = 20;

    private final ClientRepository clientRepository;
    private final BranchRepository branchRepository;
    private final AccessLogRepository accessLogRepository;
    private final CouponRepository couponRepository;

    private final NotificationService notificationService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCheckoutCompletedEvent(CheckoutCompletedEvent event) {
        log.info("Processing Loyalty check for Client: {} in Branch: {}", event.getClientId(), event.getBranchId());

        Client client = clientRepository.findByIdAndRecycleFalse(event.getClientId()).orElse(null);
        Branch branch = branchRepository.findByIdAndRecycleFalse(event.getBranchId()).orElse(null);

        if (client == null || branch == null) {
            return;
        }

        if (couponRepository.existsByClientAndBranch(client, branch)) {
            log.info("Client {} already has a loyalty coupon to Branch {}", client.getId(), branch.getId());
            return;
        }

        Double totalHours = accessLogRepository.sumDurationByClientAndBranch(client.getId(), branch.getId());

        if (totalHours != null && totalHours > REQUIRED_HOURS_FOR_COUPON) {
            String couponCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Coupon coupon = Coupon.builder()
                    .client(client)
                    .branch(branch)
                    .code(couponCode)
                    .status(CouponStatus.ACTIVE)
                    .expiredAt(OffsetDateTime.now().plusDays(10))
                    .build();

            couponRepository.save(coupon);

            String notificationMessage = String.format(
                    "¡Gracias por tu fidelidad! Has superado las %d horas en nuestra sede %s. Tu código de consumo interno es: %s",
                    REQUIRED_HOURS_FOR_COUPON, branch.getName(), couponCode);

            NotificationRequest request = new NotificationRequest(
                    client.getEmail(),
                    client.getDocument(),
                    notificationMessage,
                    branch.getName());

            notificationService.sendNotification(request);
            log.info("Loyalty coupon {} successfully emitted and notified for Client {}", couponCode, client.getId());
        }
    }
}
