package pwr.edu.pl.travelly.persistence.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pwr.edu.pl.travelly.persistence.common.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message")
public class ChatMessage extends AbstractEntity {
    @Column(name = "sender_id")
    private UUID senderId;

    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private Chat chat;

}
