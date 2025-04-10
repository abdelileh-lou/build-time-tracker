package com.time.time_traking.controller;

import com.time.time_traking.model.AttendanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AttendanceWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyNewAttendance(AttendanceRecord record) {
        messagingTemplate.convertAndSend("/topic/new-attendance", record);
    }
}