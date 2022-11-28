package pwr.edu.pl.travelly.core.chat.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private UUID uuid;
    private UUID userUuid;
    private UUID recipientUuid;
    private int newMessages;
}
