package pwr.edu.pl.travelly.core.post.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CustomPageable {
    int page;
    int size;
}
