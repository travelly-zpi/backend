package pwr.edu.pl.travelly.persistence.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pwr.edu.pl.travelly.persistence.common.AbstractEntity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
public class Chat extends AbstractEntity {
    @Column(name = "user1_id")
    private UUID user1;

    @Column(name = "user2_id")
    private UUID user2;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat")
    private List<ChatMessage> messages;
}
