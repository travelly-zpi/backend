package pwr.edu.pl.travelly.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pwr.edu.pl.travelly.core.chat.ChatFacade;
import pwr.edu.pl.travelly.core.chat.dto.ChatMessageDto;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatMessage;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatNotification;

import java.util.UUID;

@Controller
@CrossOrigin(maxAge = 3600)
public class ChatController {
    private final ChatFacade chatFacade;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(final ChatFacade chatFacade, SimpMessagingTemplate messagingTemplate) {
        this.chatFacade = chatFacade;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessage) {
        ChatMessage saved = chatFacade.processMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
                saved.getRecipientId().toString(),"/queue/messages",
                new ChatNotification(
                        saved.getUuid(),
                        saved.getSenderId()));
    }

    @GetMapping("/chat")
    public ResponseEntity<?> findOrCreateChats(@RequestParam final UUID sender, @RequestParam final UUID recipient) {
        return ResponseEntity.ok(chatFacade.findOrCreateChat(sender, recipient));
    }

    @GetMapping("/chat/{uuid}")
    public ResponseEntity<?> findUsersChats(@PathVariable final UUID uuid) {
        return ResponseEntity.ok(chatFacade.findAllByUserUUID(uuid));
    }

    @GetMapping("/chat/{uuid}/messages")
    public ResponseEntity<?> findChatMessages(@PathVariable final UUID uuid,
                                      @RequestParam(required = false, defaultValue = "false") final boolean updateStatus) {
        return ResponseEntity.ok(chatFacade.findChatMessages(uuid, updateStatus));
    }

    @GetMapping("/message/{uuid}")
    public ResponseEntity<?> findMessage(@PathVariable final UUID uuid) {
        return ResponseEntity.ok(chatFacade.findMessageByUUID(uuid));
    }
}
