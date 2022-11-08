package pwr.edu.pl.travelly.core.user;

import org.springframework.web.multipart.MultipartFile;
import pwr.edu.pl.travelly.core.user.dto.AuthResponse;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdatePasswordForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import java.io.IOException;
import java.util.UUID;


public interface UserFacade {
    UserDto save(final CreateUserForm createUserForm);
    UserDto findByUuid(final UUID uuid);
    AuthResponse generateToken(final LoginUserForm loginUserForm);
    UserDto update(final UpdateUserForm updateUserForm) throws IOException;
    void uploadImage(final MultipartFile image, final UUID userUuid) throws IOException;
    void removeImage(final UUID userUuid) throws IOException;
    void verifyUser(String token);
    void updatePassword(final UpdatePasswordForm updatePasswordForm);
    void resendVerification(final LoginUserForm loginUserForm);
}
