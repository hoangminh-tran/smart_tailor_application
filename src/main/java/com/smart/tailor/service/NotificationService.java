package com.smart.tailor.service;

import com.smart.tailor.entities.Notification;
import com.smart.tailor.utils.request.NotificationRequest;
import com.smart.tailor.utils.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendGlobalNotification(NotificationRequest notificationRequest);

    void sendPrivateNotification(NotificationRequest notificationRequest);

    Notification saveNotification(NotificationRequest notificationRequest);

    List<NotificationResponse> getNotificationByUserID(String jwtToken, String userID) throws Exception;

    List<NotificationResponse> markAllRead(String jwtToken, String userID) throws Exception;

    void updateNotificationStatus(String notificationID);
}
