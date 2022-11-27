package pwr.edu.pl.travelly.persistence.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.edu.pl.travelly.persistence.chat.entity.Chat;
import pwr.edu.pl.travelly.persistence.chat.entity.ChatMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findByUuid(final UUID uuid);
    List<ChatMessage> findByChatOrderByIdAsc(final Chat chat);
    List<ChatMessage> findByChatAndStatus(final Chat chat, final String status);
    long countByChatAndRecipientIdAndStatus(final Chat chat, final UUID recipientId, final String status);
}
