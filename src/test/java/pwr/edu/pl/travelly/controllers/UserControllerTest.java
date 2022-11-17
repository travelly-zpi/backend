package pwr.edu.pl.travelly.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pwr.edu.pl.travelly.api.controllers.UserController;
import pwr.edu.pl.travelly.core.common.exception.ExistsException;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.user.UserFacade;
import pwr.edu.pl.travelly.core.user.dto.AuthResponse;
import pwr.edu.pl.travelly.core.user.dto.LoggedUserDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserFacade userFacade;
    CreateUserForm createUserForm = new CreateUserForm();
    UserDto userDto = new UserDto();

    @BeforeAll
    public static void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void generateToken_validInput_returnsStatus200() {
        LoginUserForm loginUserForm = new LoginUserForm();
        when(userFacade.generateToken(loginUserForm)).thenReturn(new AuthResponse("token", 1234567890, new LoggedUserDto()));

        ResponseEntity<?> responseEntity = userController.generateToken(loginUserForm);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void saveUser_validInput_returnStatusCreated() {
        when(userFacade.save(createUserForm)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.saveUser(createUserForm);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void saveUser_null_returnsBadRequest() {
        when(userFacade.save(createUserForm)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.saveUser(createUserForm);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findUser_validInput_returnsStatusOk() {
        UUID id = UUID.randomUUID();
        when(userFacade.findByUuid(id)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.findUser(id);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void updateUser_validInput_returnsStatusOk() throws IOException {
        UpdateUserForm updateUserForm = new UpdateUserForm();
        when(userFacade.update(updateUserForm)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.updateUser(updateUserForm);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void updateUser_invalidInput_returnsNotFound() throws IOException {
        UpdateUserForm updateUserForm = new UpdateUserForm();
        when(userFacade.update(updateUserForm)).thenThrow(new ExistsException("EMAIL_EXISTS"));
        when(userFacade.update(null)).thenThrow(new NotFoundException("User not found"));

        assertThrows(ExistsException.class, () -> userController.updateUser(updateUserForm));
        assertThrows(NotFoundException.class, () -> userController.updateUser(null));
    }

}
