package pwr.edu.pl.travelly.core.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private UUID uuid;
    private String title;
    private String description;
    private String mainImage;
    private String type;
}
