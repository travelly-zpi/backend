package pwr.edu.pl.travelly.persistence.user.entity;

import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;

public class UserMapper {

    public static User toEntity(final CreateUserForm registerUserForm) {
        return new User().toBuilder()
                .userName(registerUserForm.getUserName())
                .email(registerUserForm.getEmail())
                .password(registerUserForm.getPassword())
                .dateOfBirth(registerUserForm.getDateOfBirth())
                .firstName(registerUserForm.getFirstName())
                .lastName(registerUserForm.getLastName())
                .hobbies(registerUserForm.getHobbies())
                .languages(registerUserForm.getLanguages())
                .build();
    }

    public static UserDto toDto(final User user) {
        return UserDto.builder()
                .uuid(user.getUuid())
                .userName(user.getUserName())
                .password(user.getPassword())
                .role(user.getRole().getName())
                .build();
    }
}
