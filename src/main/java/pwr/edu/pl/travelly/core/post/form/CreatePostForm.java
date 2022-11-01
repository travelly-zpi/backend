package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class CreatePostForm {

    @NotBlank(message = "BLANK_TITLE")
    private String title;

    //@NotBlank(message = "BLANK_DESCRIPTION")
    private String description;

    //@NotBlank(message = "BLANK_ACTIVE_FROM")
    private String activeFrom;

    //@NotBlank(message = "BLANK_ACTIVE_TO")
    private String activeTo;

    //@NotBlank(message = "BLANK_ACTIVE_TO")
    private String type;

    //@NotNull(message = "BLANK_PARTICIPANTS")
    private int participants;

    //@NotBlank(message = "BLANK_START_POINT")
    private String startPoint;

    //@NotBlank(message = "BLANK_END_POINT")
    private String endPoint;

    //@NotNull(message = "BLANK_AUTHOR")
    private UUID author;

    //@NotNull(message = "BLANK_PROFILE_IMAGE")
    private MultipartFile profileImage;

    private List<MultipartFile> images;
}
