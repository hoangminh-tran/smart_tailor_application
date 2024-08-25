package com.smart.tailor.event;

import com.smart.tailor.entities.User;
import com.smart.tailor.enums.TypeOfVerification;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private TypeOfVerification typeOfVerification;

    public RegistrationCompleteEvent(User user, TypeOfVerification typeOfVerification) {
        super(user);
        this.user = user;
        this.typeOfVerification = typeOfVerification;
    }
}
