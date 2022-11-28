package pwr.edu.pl.travelly.persistence.chat.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {
    private UUID uuid;
    private UUID senderId;
}
