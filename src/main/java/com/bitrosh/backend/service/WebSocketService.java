package com.bitrosh.backend.service;

import com.bitrosh.backend.dto.core.ChatParticipantChange;
import com.bitrosh.backend.dto.core.ChatResDtoWithWorkspace;
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

    public void notifyCreate(String username, ChatResDtoWithWorkspace message) {
        messagingTemplate.convertAndSend(CREATE.formatted(username, "chat"), message);
    }

    public void notifyUpdate(String username, ChatResDtoWithWorkspace message) {
        messagingTemplate.convertAndSend(UPDATE.formatted(username, "chat"), message);
    }

    public void notifyDelete(String username, ChatResDtoWithWorkspace message) {
        messagingTemplate.convertAndSend(DELETE.formatted(username, "chat"), message);
    }

    public void notifyAdd(String username, ChatParticipantChange change) {
        messagingTemplate.convertAndSend(CREATE.formatted(username, "participant/add"), change);
    }

    public void notifyRemove(String username, ChatParticipantChange change) {
        messagingTemplate.convertAndSend(CREATE.formatted(username, "participant/remove"), change);
    }

}
