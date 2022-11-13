package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class SavePostForm {

    private String title;
    private String description;
    private String activeFrom;
    private String activeTo;
    private String type;
    private Boolean active;
    private int participants;
    private String startPoint;
    private String endPoint;
    private UUID author;
}
