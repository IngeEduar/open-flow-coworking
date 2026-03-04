package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.config.NotificationsConfig;
import com.nelumbo.open_flow_coworking.model.request.notification.NotificationRequest;
import com.nelumbo.open_flow_coworking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final RestTemplate restTemplate;
    private final NotificationsConfig notificationsConfig;

    @Override
    public void sendNotification(NotificationRequest request) {
        log.info("Sending notification to {}", notificationsConfig.getUrl());
        log.info("Payload: {}", request);

        HttpHeaders headers = new HttpHeaders();
        if (notificationsConfig.getExtraHeaders() != null) {
            notificationsConfig.getExtraHeaders().forEach(header -> headers.add(header.getName(), header.getValue()));
        }

        HttpEntity<NotificationRequest> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.postForEntity(notificationsConfig.getUrl(), entity, String.class);
            log.info("Notification sent successfully");
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
        }
    }
}
