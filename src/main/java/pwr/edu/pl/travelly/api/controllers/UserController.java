package pwr.edu.pl.travelly.api.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pwr.edu.pl.travelly.core.user.UserFacade;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import static java.util.Objects.nonNull;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/user")
public class UserController{

    private final UserFacade userFacade;

    public UserController(@Qualifier("userFacade") final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody final LoginUserForm loginUserForm) throws AuthenticationException {
        return ResponseEntity.ok(this.userFacade.generateToken(loginUserForm));
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody final CreateUserForm user){
        return nonNull(userFacade.save(user)) ? ResponseEntity.ok(user) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody final UpdateUserForm userForm){
        return ResponseEntity.ok(userFacade.update(userForm));
    }

}
