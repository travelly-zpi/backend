package pwr.edu.pl.travelly.core.chat.port;

import pwr.edu.pl.travelly.core.chat.dto.ChatDto;
import pwr.edu.pl.travelly.core.chat.dto.ChatMessageDto;
import pwr.edu.pl.travelly.persistence.chat.entity.Chat;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatPort {
    Chat findByUUID(final UUID uuid);
    UUID findOrCreateChat(UUID sender, UUID recipient);
    ChatMessage findMessageByUUID(UUID uuid);
    List<ChatDto> findAllByUserUUID(final UUID userUuid);
    List<ChatMessage> findChatMessages(final Chat chat);
    void delete(final UUID uuid);
    ChatMessage save(final ChatMessageDto message);
    void updateMessageStatus(final Chat chat);
}
