package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class PostFilterForm {
    private String localisation;
    private LocalDate startDate;
    private LocalDate endDate;
    private int participants;
    private String startPoint;
    private String endPoint;
}
