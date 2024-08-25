package com.smart.tailor.utils.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    @Nullable
    private String senderID;
    @Nullable
    private String recipientID;
    private String action;
    private String type;
    private String targetID;
    private String message;
}