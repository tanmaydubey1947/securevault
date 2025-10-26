package com.securevault.service.notification;

import com.securevault.model.dto.notification.NotificationRequest;

public interface NotificationService {

    void sendNotification(NotificationRequest request);
}
