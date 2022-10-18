package pwr.edu.pl.travelly.core.user.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CreateUserForm {

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
