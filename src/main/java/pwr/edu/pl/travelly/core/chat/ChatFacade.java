package pwr.edu.pl.travelly.core.chat;

import pwr.edu.pl.travelly.core.chat.dto.ChatDto;
import pwr.edu.pl.travelly.core.chat.dto.ChatMessageDto;
import pwr.edu.pl.travelly.persistence.chat.entity.Chat;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatFacade {
    UUID findOrCreateChat(UUID sender, UUID recipient);
    List<ChatMessage> findChatMessages(UUID uuid, boolean updateStatus);
    ChatMessage findMessageByUUID(UUID uuid);
    List<ChatDto> findAllByUserUUID(final UUID userUuid);
    void delete(final UUID uuid);
    ChatMessage processMessage(ChatMessageDto message);
}
