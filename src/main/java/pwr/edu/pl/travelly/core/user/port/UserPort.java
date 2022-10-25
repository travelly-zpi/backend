package pwr.edu.pl.travelly.core.user.port;

import pwr.edu.pl.travelly.core.user.dto.LoggedUserDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import java.util.UUID;

public interface UserPort {
    UserDto save(final CreateUserForm createUserForm);
    UserDto update(final UpdateUserForm updateUserForm);
    LoggedUserDto findByUserName(final String email);
    UserDto findByUuid(final UUID uuid);
    UserDto findByEmail(final String email);

    boolean existsByUserName(final String email);
    boolean existsByUserNameAndUuidNot(final String email, final UUID uuid);

    void enableUser(String userName);
}
