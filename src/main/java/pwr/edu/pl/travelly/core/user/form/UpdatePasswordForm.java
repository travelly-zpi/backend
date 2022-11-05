package pwr.edu.pl.travelly.core.user.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePasswordForm {
    public UUID uuid;
    public String oldPassword;
    public String newPassword;
}


