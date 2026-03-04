package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.model.request.notification.NotificationRequest;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
}
