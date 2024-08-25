package com.smart.tailor.service.impl;

import com.smart.tailor.config.DataHandler;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Notification;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.NotificationMapper;
import com.smart.tailor.repository.NotificationRepository;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.service.NotificationService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.NotificationRequest;
import com.smart.tailor.utils.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends TextWebSocketHandler implements NotificationService {
    private final DataHandler dataHandler;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final JwtService jwtService;

    @Override
    public void sendGlobalNotification(NotificationRequest notificationRequest) {
//        dataHandler.sendGlobal(notificationRequest);
        var userList = userService.getAllUserResponse();
        var admin = userService.getUserByEmail("adminsmarttailor123@gmail.com");
        for (var user : userList) {
            notificationRequest.setSenderID(admin.getUserID());
            notificationRequest.setRecipientID(user.getUserID());
            sendPrivateNotification(notificationRequest);
        }
    }

    @Override
    public void sendPrivateNotification(NotificationRequest notificationRequest) {
        var savedData = saveNotification(notificationRequest);
        dataHandler.sendToUser(notificationRequest.getRecipientID(), notificationMapper.mapToNotificationResponse(savedData));
    }

    @Override
    public Notification saveNotification(NotificationRequest notificationRequest) {

        var sender = userService.getUserByUserID(notificationRequest.getSenderID()).orElseThrow(() -> {
            return new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND + ": " + notificationRequest.getSenderID());
        });
        var recipient = userService.getUserByUserID(notificationRequest.getRecipientID()).orElseThrow(() -> {
            return new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND + ": " + notificationRequest.getRecipientID());
        });

        return notificationRepository.save(Notification.builder().action(notificationRequest.getAction()).type(notificationRequest.getType())

                .sender(sender).recipient(recipient)

                .targetID(notificationRequest.getTargetID()).status(false).message(notificationRequest.getMessage()).build());
    }

    @Override
    public List<NotificationResponse> getNotificationByUserID(String jwtToken, String userID) throws Exception {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        return notificationRepository
                .findAll()
                .stream()
                .filter(noti -> noti.getRecipient().getUserID().equals(userID))
                .map(notificationMapper::mapToNotificationResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> markAllRead(String jwtToken, String userID) throws Exception{
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        var listNoti = getNotificationByUserID(jwtToken, userID);
        if (!listNoti.isEmpty()) {
            listNoti.stream().forEach(noti -> {
                updateNotificationStatus(noti.getNotificationID());
            });
            listNoti = getNotificationByUserID(jwtToken, userID);
        }
        return listNoti;
    }

    @Override
    public void updateNotificationStatus(String notificationID) {
        var noti = notificationRepository.findById(notificationID).orElseThrow(() -> {
            return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND + ": " + notificationID);
        });
        noti.setStatus(true);
        notificationRepository.save(noti);
    }

}