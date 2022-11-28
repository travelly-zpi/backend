package pwr.edu.pl.travelly.core.chat;

import org.springframework.stereotype.Service;
import pwr.edu.pl.travelly.core.chat.dto.ChatDto;
import pwr.edu.pl.travelly.core.chat.dto.ChatMessageDto;
import pwr.edu.pl.travelly.core.chat.port.ChatPort;
import pwr.edu.pl.travelly.persistence.chat.entity.Chat;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatMessage;

import java.util.List;
import java.util.UUID;

@Service
public class ChatFacadeImpl implements ChatFacade{

    private final ChatPort chatPort;

    public ChatFacadeImpl(final ChatPort chatPort) {
        this.chatPort = chatPort;
    }

    @Override
    public UUID findOrCreateChat(UUID sender, UUID recipient) {
        return chatPort.findOrCreateChat(sender, recipient);
    }

    @Override
    public List<ChatMessage> findChatMessages(UUID uuid, boolean updateStatus) {
        Chat chat = findByUUID(uuid, updateStatus);
        return chatPort.findChatMessages(chat);
    }

    @Override
    public ChatMessage findMessageByUUID(UUID uuid) {
        return chatPort.findMessageByUUID(uuid);
    }

    @Override
    public List<ChatDto> findAllByUserUUID(UUID userUuid) {
        return chatPort.findAllByUserUUID(userUuid);
    }

    @Override
    public void delete(UUID uuid) {
        chatPort.delete(uuid);
    }

    @Override
    public ChatMessage processMessage(ChatMessageDto message) {
        // check if chat exist: if not create and pass cht id to message before save
        return  chatPort.save(message);
    }

    public Chat findByUUID(UUID uuid, boolean updateStatus) {
        Chat chat = chatPort.findByUUID(uuid);
        if (updateStatus) chatPort.updateMessageStatus(chat);
        return chat;
    }
}
