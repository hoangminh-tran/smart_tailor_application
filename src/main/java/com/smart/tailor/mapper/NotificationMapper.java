package com.smart.tailor.mapper;

import com.smart.tailor.entities.Notification;
import com.smart.tailor.utils.response.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface NotificationMapper {
    @Mapping(source = "notification.createDate", target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "notification.lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    NotificationResponse mapToNotificationResponse(Notification notification);
}
