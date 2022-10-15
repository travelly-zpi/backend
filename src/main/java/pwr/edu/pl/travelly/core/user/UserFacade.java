package pwr.edu.pl.travelly.core.user;

import pwr.edu.pl.travelly.core.user.dto.AuthResponse;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

public interface UserFacade {
    UserDto save(final CreateUserForm createUserForm);
    AuthResponse generateToken(final LoginUserForm loginUserForm);
    UserDto update(final UpdateUserForm updateUserForm);
}
