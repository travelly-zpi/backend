package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class SavePostForm {

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

    private Boolean active;

    //@NotNull(message = "BLANK_PARTICIPANTS")
    private int participants;

    //@NotBlank(message = "BLANK_START_POINT")
    private String startPoint;

    //@NotBlank(message = "BLANK_END_POINT")
    private String endPoint;

    //@NotNull(message = "BLANK_AUTHOR")
    private UUID author;
}
