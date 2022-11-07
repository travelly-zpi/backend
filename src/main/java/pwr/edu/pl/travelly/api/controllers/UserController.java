package pwr.edu.pl.travelly.api.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pwr.edu.pl.travelly.core.user.UserFacade;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdatePasswordForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.nonNull;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    private final UserFacade userFacade;

    public UserController(@Qualifier("userFacade") final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody @Valid final LoginUserForm loginUserForm) throws AuthenticationException {
        return ResponseEntity.ok(this.userFacade.generateToken(loginUserForm));
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody @Valid final CreateUserForm user){
        return nonNull(userFacade.save(user)) ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value="/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> findUser(@PathVariable final UUID uuid){
        final UserDto user = userFacade.findByUuid(uuid);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid final UpdateUserForm userForm) throws IOException {
        return ResponseEntity.ok(userFacade.update(userForm));
    }

    @PutMapping(value="/{uuid}/uploadProfileImage")
    public ResponseEntity<?> uploadUserProfileImage(@RequestBody final MultipartFile image, @PathVariable("uuid") UUID userUuid) throws IOException {
        userFacade.uploadImage(image, userUuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(value="/{uuid}/removeProfileImage")
    public ResponseEntity<?> removeUserProfileImage(@PathVariable("uuid") UUID userUuid) throws IOException {
        userFacade.removeImage(userUuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/changePassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody @Valid final UpdatePasswordForm passwordForm) {
        userFacade.updatePassword(passwordForm);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/register/verify", method = RequestMethod.GET)
    public ResponseEntity<?> verifyCustomer(@RequestParam(required = false) String token){
        userFacade.verifyUser(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="register/verify", method = RequestMethod.POST)
    public ResponseEntity<?> resendVerification(@RequestBody @Valid final LoginUserForm loginUserForm){
        userFacade.resendVerification(loginUserForm);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
