package com.bitrosh.backend.service;

import com.bitrosh.backend.dto.core.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String CREATE = "/topic/%s/queue/create/%s";
    private static final String UPDATE = "/topic/%s/queue/update/%s";
    private static final String DELETE = "/topic/%s/queue/delete/%s";

    public void notifyCreate(String username, MessageDto message) {
        messagingTemplate.convertAndSend(CREATE.formatted(username, "message"), message);
    }

    public void notifyUpdate(String username, MessageDto message) {
        messagingTemplate.convertAndSend(UPDATE.formatted(username, "message"), message);
    }

    public void notifyDelete(String username, MessageDto message) {
        messagingTemplate.convertAndSend(DELETE.formatted(username, "message"), message);
    }

}
