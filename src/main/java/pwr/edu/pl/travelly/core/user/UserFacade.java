package pwr.edu.pl.travelly.core.user;

import pwr.edu.pl.travelly.core.user.dto.AuthResponse;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;

public interface UserFacade {
    UserDto save(final CreateUserForm user);
    AuthResponse generateToken(LoginUserForm loginUserForm);
}
