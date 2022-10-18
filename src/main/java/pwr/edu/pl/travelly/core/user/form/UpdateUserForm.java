package pwr.edu.pl.travelly.core.user.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserForm {
    private UUID uuid;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String languages;
    private String hobbies;

    private String country;
    private String city;
}
