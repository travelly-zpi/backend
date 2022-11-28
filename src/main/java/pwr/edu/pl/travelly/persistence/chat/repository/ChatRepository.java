package pwr.edu.pl.travelly.persistence.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.edu.pl.travelly.persistence.chat.entity.Chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUuid(final UUID uuid);
    List<Chat> findByUser1(final UUID user1);
    List<Chat> findByUser2(final UUID user2);
    Boolean existsChatByUser1AndUser2(final UUID user1, final UUID user2);
    Chat findChatByUser1AndUser2(final UUID user1, final UUID user2);
}
