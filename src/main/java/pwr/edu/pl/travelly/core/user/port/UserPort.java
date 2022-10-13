package pwr.edu.pl.travelly.core.user.port;

import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;

import java.util.UUID;

public interface UserPort {
    boolean existsByEmail(final String email);
    UserDto findByUserName(final String userName);
    boolean existsByUuid(final UUID uuid);
    boolean existsByEmailAndUuidNot(final String login, final UUID uuid);
    UserDto findByEmail(final String email);
    UserDto save(final CreateUserForm userDto);
}
