package com.nelumbo.open_flow_coworking.model.request.notification;

public record NotificationRequest(
        String email,
        String document,
        String message,
        String branch
) {
}
