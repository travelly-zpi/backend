package pwr.edu.pl.travelly.core.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class PostAttachmentDto {
    private UUID attachmentUuid;
    private Boolean isMain;
    private String url;
}
