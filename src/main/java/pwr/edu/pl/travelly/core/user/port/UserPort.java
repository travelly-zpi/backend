package pwr.edu.pl.travelly.core.user.port;

import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import java.util.UUID;

public interface UserPort {
    UserDto findByUserName(final String userName);
    UserDto save(final CreateUserForm createUserForm);
    UserDto update(final UpdateUserForm updateUserForm);

    boolean existsByEmail(final String email);
    boolean existsByUuid(final UUID uuid);
    boolean existsByUserName(final String userName);
    boolean existsByEmailAndUuidNot(final String email, final UUID uuid);
    boolean existsByUserNameAndUuidNot(final String userName, final UUID uuid);

}
