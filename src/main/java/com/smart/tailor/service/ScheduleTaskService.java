package com.smart.tailor.service;

public interface ScheduleTaskService {
    void deleteUserWithEmailUnverifiedSchedule();

    void checkValidOrderAfterExpirationTimeOrder() throws Exception;

    void updatePayOS() throws Exception;

    void createOrderDelivery() throws Exception;
}
