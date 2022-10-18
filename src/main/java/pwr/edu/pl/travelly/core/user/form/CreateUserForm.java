package pwr.edu.pl.travelly.core.user.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Setter
@Getter
public class CreateUserForm {

    @NotBlank(message = "BLANK_EMAIL")
    @Size(min = 3, max = 50, message = "SIZE_EMAIL")
    @Email(message = "INVALID_FORMAT_EMAIL")
    private String email;

    @NotBlank(message = "BLANK_PASSWORD")
    @Size(min = 8, max = 50, message = "SIZE_PASSWORD")
    private String password;

    @NotBlank(message = "BLANK_FIRST_NAME")
    private String firstName;

    @NotBlank(message = "BLANK_LAST_NAME")
    private String lastName;
}
