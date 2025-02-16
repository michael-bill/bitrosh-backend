package com.bitrosh.backend.service;

import com.bitrosh.backend.dto.core.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String CREATE = "queue/create/";
    private static final String UPDATE = "queue/update/";
    private static final String DELETE = "queue/delete/";

    public MessageDto notifyCreate(String username, MessageDto message) {
        messagingTemplate.convertAndSendToUser(username, CREATE + "message", message);
        return message;
    }

    public MessageDto notifyUpdate(String username, MessageDto message) {
        messagingTemplate.convertAndSendToUser(username, UPDATE + "message", message);
        return message;
    }

    public void notifyDelete(String username, MessageDto message) {
        messagingTemplate.convertAndSendToUser(username, DELETE + "message", message);
    }

}
