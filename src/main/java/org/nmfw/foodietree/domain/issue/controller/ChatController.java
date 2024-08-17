package org.nmfw.foodietree.domain.issue.controller;

import org.nmfw.foodietree.domain.issue.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage/{issueId}")
    @SendTo("/topic/messages/{issueId}")
    public ChatMessage sendMessage(@DestinationVariable String issueId, ChatMessage message) {
        // 서버에서 메시지 처리 후, 클라이언트로 전송
        return new ChatMessage(issueId, message.getContent(), message.getSender());
    }
}
