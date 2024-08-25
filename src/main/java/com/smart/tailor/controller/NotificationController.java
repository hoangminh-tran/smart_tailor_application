package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.NotificationService;
import com.smart.tailor.utils.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.NotificationAPI.Notification)
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping(APIConstant.NotificationAPI.SEND_PUBLIC_NOTIFICATION)
    public ResponseEntity<ObjectNode> sendGlobalNotification(@RequestBody NotificationRequest notificationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            notificationService.sendGlobalNotification(notificationRequest);
            respon.put("status", 200);
            respon.put("message", MessageConstant.SEND_NOTIFICATION_SUCCESSFULLY);
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", ex.getMessage());
            logger.error("ERROR IN SEND PUBLIC NOTIFICATION. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.NotificationAPI.SEND_NOTIFICATION)
    public ResponseEntity<ObjectNode> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            notificationService.sendPrivateNotification(notificationRequest);
            respon.put("status", 200);
            respon.put("message", MessageConstant.SEND_NOTIFICATION_SUCCESSFULLY);
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", ex.getMessage());
            logger.error("ERROR IN SEND NOTIFICATION. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.NotificationAPI.GET_ALL_NOTIFICATION_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllNotification(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                         @PathVariable("userID") String userID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        var data = notificationService.getNotificationByUserID(jwtToken, userID);
        respon.put("status", 200);
        respon.put("message", MessageConstant.GET_NOTIFICATION_SUCCESSFULLY);
        respon.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(respon);
    }

    @PutMapping(APIConstant.NotificationAPI.UPDATE_NOTIFICATION_STATUS + "/{notiID}")
    public ResponseEntity<ObjectNode> updateNotificationStatus(@PathVariable("notiID") String notiID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            notificationService.updateNotificationStatus(notiID);
            respon.put("status", 200);
            respon.put("message", MessageConstant.UPDATE_NOTIFICATION_STATUS_SUCCESSFULLY);
//            respon.set("data", objectMapper.valueToTree(data));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", ex.getMessage());
            logger.error("ERROR IN GET NOTIFICATION. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.NotificationAPI.MARK_ALL_READ + "/{userID}")
    public ResponseEntity<ObjectNode> markAllRead(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                  @PathVariable("userID") String userID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            var data = notificationService.markAllRead(jwtToken, userID);
            respon.put("status", 200);
            respon.put("message", MessageConstant.MARK_ALL_READ);
            respon.set("data", objectMapper.valueToTree(data));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", ex.getMessage());
            logger.error("ERROR IN GET NOTIFICATION. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }
}
