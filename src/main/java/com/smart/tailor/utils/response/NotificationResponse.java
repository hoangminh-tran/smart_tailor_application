package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String notificationID;
    private UserResponse sender;
    private UserResponse recipient;
    private String action;
    private String type;
    private String targetID;
    private String message;
    private Boolean status;
    private String createDate;
    private String lastModifiedDate;
}
