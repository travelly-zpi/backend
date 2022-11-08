package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class PostFilterForm {
    private int page;
    private int size;
    private String startDate;
    private String endDate;
    private Boolean active;
    private Integer participants;
    private String startPoint;
    private String endPoint;
    private UUID author;
}
