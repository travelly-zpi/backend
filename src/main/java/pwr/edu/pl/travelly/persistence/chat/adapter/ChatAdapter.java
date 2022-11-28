package pwr.edu.pl.travelly.persistence.chat.adapter;

import org.springframework.stereotype.Service;
import pwr.edu.pl.travelly.core.chat.dto.ChatDto;
import pwr.edu.pl.travelly.core.chat.dto.ChatMessageDto;
import pwr.edu.pl.travelly.core.chat.port.ChatPort;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.persistence.chat.entity.Chat;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatMessage;
import pwr.edu.pl.travelly.persistence.chat.entity.MessageStatus;
import pwr.edu.pl.travelly.persistence.chat.repository.ChatMessageRepository;
import pwr.edu.pl.travelly.persistence.chat.repository.ChatRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatAdapter implements ChatPort {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository messageRepository;

    public ChatAdapter(final ChatRepository chatRepository,
                       final ChatMessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Chat findByUUID(UUID uuid) {
        //if (uuid == null) return null;
        return chatRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("CHAT_NOT_FOUND"));
    }

    @Override
    public UUID findOrCreateChat(UUID sender, UUID recipient) {
        if (chatRepository.existsChatByUser1AndUser2(sender, recipient)) {
            return chatRepository.findChatByUser1AndUser2(sender, recipient).getUuid();
        }
        if (chatRepository.existsChatByUser1AndUser2(recipient, sender)) {
            return chatRepository.findChatByUser1AndUser2(recipient, sender).getUuid();
        }
        Chat chat = startChat(sender.toString(), recipient.toString());
        return chat.getUuid();
    }

    @Override
    public ChatMessage findMessageByUUID(UUID uuid) {
        ChatMessage message = messageRepository.findByUuid(uuid)
                .orElseThrow(() ->  new NotFoundException("MESSAGE_NOT_FOUND"));
        message.setStatus(MessageStatus.DELIVERED.toString());
        return  messageRepository.save(message);
    }

    @Override
    public List<ChatDto> findAllByUserUUID(UUID userUuid) {
        List<ChatDto> dtos = chatRepository.findByUser1(userUuid).stream()
                .map(this::toDto).collect(Collectors.toList());
        dtos.addAll(chatRepository.findByUser2(userUuid).stream()
                .map(this::toDtoSwitchUsers).toList());
        return dtos;
    }

    @Override
    public List<ChatMessage> findChatMessages(Chat chat) {
        return messageRepository.findByChatOrderByIdAsc(chat);
    }

    @Override
    public void delete(final UUID uuid) {
        final Chat chat = findByUUID(uuid);
        chatRepository.delete(chat);
    }

    @Override
    public ChatMessage save(final ChatMessageDto message) {
        Chat chat = message.getChatId() == null ? null : findByUUID(UUID.fromString(message.getChatId()));
        if (chat == null) {
            chat = startChat(message.getSenderId(), message.getRecipientId());
        }
        ChatMessage saveMsg = toMessage(message);
        saveMsg.setUuid(UUID.randomUUID());
        saveMsg.setStatus(MessageStatus.RECEIVED.toString());
        saveMsg.setChat(chat);
        return messageRepository.save(saveMsg);
    }

    @Override
    public void updateMessageStatus(final Chat chat) {
        messageRepository.findByChatAndStatus(chat, MessageStatus.RECEIVED.toString()).forEach(this::updateMessage);
    }

    private void updateMessage(final ChatMessage message) {
        message.setStatus(MessageStatus.DELIVERED.toString());
        messageRepository.save(message);
    }

    private Chat startChat(final String senderId, final String recipientId) {
        Chat chat = Chat.builder()
                .uuid(UUID.randomUUID())
                .user1(UUID.fromString(senderId))
                .user2(UUID.fromString(recipientId))
                .build();
        return chatRepository.save(chat);
    }

    public ChatMessage toMessage(ChatMessageDto msgDto) {
        return ChatMessage.builder()
                .senderId(UUID.fromString(msgDto.getSenderId()))
                .recipientId(UUID.fromString(msgDto.getRecipientId()))
                .content(msgDto.getContent())
                .timestamp(LocalDate.parse(msgDto.getTimestamp()))
                .build();
    }

    public ChatDto toDto(Chat chat) {
        long newMessages = messageRepository.countByChatAndRecipientIdAndStatus(chat,
                chat.getUser1(), "RECEIVED");
        return ChatDto.builder()
                .uuid(chat.getUuid())
                .userUuid(chat.getUser1())
                .recipientUuid(chat.getUser2())
                .newMessages((int) newMessages)
                .build();
    }

    public ChatDto toDtoSwitchUsers(Chat chat) {
        long newMessages = messageRepository.countByChatAndRecipientIdAndStatus(chat,
                chat.getUser2(), "RECEIVED");
        return ChatDto.builder()
                .uuid(chat.getUuid())
                .userUuid(chat.getUser2())
                .recipientUuid(chat.getUser1())
                .newMessages((int) newMessages)
                .build();
    }
}
