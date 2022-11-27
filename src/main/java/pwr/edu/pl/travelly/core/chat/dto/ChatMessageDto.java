package pwr.edu.pl.travelly.core.chat.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private String timestamp;
}
