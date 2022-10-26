package pwr.edu.pl.travelly.core.user.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserForm {

    @NotNull(message = "BLANK_UUID")
    private UUID uuid;

    @NotBlank(message = "BLANK_EMAIL")
    @Size(min = 3, max = 50, message = "SIZE_EMAIL")
    @Email(message = "INVALID_FORMAT_EMAIL")
    private String email;

    @NotBlank(message = "BLANK_FIRST_NAME")
    private String firstName;

    @NotBlank(message = "BLANK_LAST_NAME")
    private String lastName;

    private String password;
    private LocalDate dateOfBirth;
    private String languages;
    private String description;
    private MultipartFile image;
    private String localisation;
}
