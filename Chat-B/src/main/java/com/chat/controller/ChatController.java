package com.chat.controller;

import com.chat.model.Message;
import com.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public Message sendMessage(@PathVariable String roomId, @Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        System.out.println("Message : "+message);
        messagingTemplate.convertAndSend("/topic/{roomId}", message);
        return messageRepository.save(message);
    }

    @GetMapping("/history")
    public List<Message> getChatHistory() {
        return messageRepository.findAllByOrderByTimestampDesc();
    }

}
