package pwr.edu.pl.travelly.persistence.user.entity;

import pwr.edu.pl.travelly.core.localisation.dto.LocalisationDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.persistence.localisation.entity.Localisation;
import pwr.edu.pl.travelly.persistence.localisation.entity.LocalisationMapper;

public class UserMapper {

    public static User toEntity(final CreateUserForm registerUserForm) {
        return new User().toBuilder()
                .userName(registerUserForm.getUserName())
                .email(registerUserForm.getEmail())
                .password(registerUserForm.getPassword())
                .firstName(registerUserForm.getFirstName())
                .lastName(registerUserForm.getLastName())
                .build();
    }

    public static UserDto toDto(final User user) {
        return UserDto.builder()
                .uuid(user.getUuid())
                .userName(user.getUserName())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .hobbies(user.getHobbies())
                .languages(user.getLanguages())
                .password(user.getPassword())
                .role(user.getRole().getName())
                .localisation(LocalisationMapper.toDto(user.getLocalisation()))
                .build();
    }
}
