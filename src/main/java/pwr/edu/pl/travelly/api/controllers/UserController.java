package pwr.edu.pl.travelly.api.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import pwr.edu.pl.travelly.core.user.UserFacade;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import javax.validation.Valid;
import java.util.UUID;

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

    @RequestMapping(value="/hello_world", method = RequestMethod.GET)
    public String helloWorld(){
        return "HELLO_WORLD";
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody final UpdateUserForm userForm){
        return ResponseEntity.ok(userFacade.update(userForm));
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
