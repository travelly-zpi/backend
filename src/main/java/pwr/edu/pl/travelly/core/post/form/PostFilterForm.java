package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PostFilterForm {
    private String startDate;
    private String endDate;
    private Integer participants;
    private String startPoint;
    private String endPoint;
}
