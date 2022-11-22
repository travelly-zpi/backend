package pwr.edu.pl.travelly.core.post.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PostFilterForm {
    
    private int page;
    private int size;
    private String title;
    private String startDate;
    private String date;
    private String endDate;
    private Boolean active;
    private String type;
    private Integer participants;
    private String startPoint;
    private String endPoint;
    private UUID author;
    private UUID notAuthor;
}
