package pwr.edu.pl.travelly.persistence.user.adapter;

import org.springframework.stereotype.Component;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.user.dto.LoggedUserDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;
import pwr.edu.pl.travelly.core.user.port.UserPort;
import pwr.edu.pl.travelly.persistence.localisation.entity.Localisation;
import pwr.edu.pl.travelly.persistence.localisation.repository.LocalisationRepository;
import pwr.edu.pl.travelly.persistence.user.entity.Role;
import pwr.edu.pl.travelly.persistence.user.entity.User;
import pwr.edu.pl.travelly.persistence.user.entity.UserMapper;
import pwr.edu.pl.travelly.persistence.user.repository.RoleRepository;
import pwr.edu.pl.travelly.persistence.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Component
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LocalisationRepository localisationRepository;

    public UserAdapter(final UserRepository userRepository,
                       final RoleRepository roleRepository,
                       final LocalisationRepository localisationRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.localisationRepository = localisationRepository;
    }

    @Override
    @Transactional
    public LoggedUserDto findByUserName(final String userName) {
        final User user = userRepository
                .findUserByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toLoggedUserDto(user);
    }

    @Override
    @Transactional
    public UserDto findByUuid(final UUID uuid) {
        final User user = userRepository
                .findUserByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public boolean existsByUserName(final String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    @Transactional
    public boolean existsByUserNameAndUuidNot(final String userName,final UUID uuid) {
        return userRepository.existsByUserNameAndUuidNot(userName, uuid);
    }

    @Override
    @Transactional
    public UserDto save(final CreateUserForm registerUserForm) {
        final User user = UserMapper.toEntity(registerUserForm);
        user.setUuid(UUID.randomUUID());
        fetchNewUserDependencies(user, registerUserForm);
        final User newUser = userRepository.save(user);
        return UserMapper.toDto(newUser);
    }

    private void fetchNewUserDependencies(final User user, final CreateUserForm registerUserForm) {
        user.setRole(fetchRoleDependency());
    }

    private void fetchUpdateUserDependencies(final User user, final UpdateUserForm updateUserForm) {
        user.setRole(fetchRoleDependency());
        user.setLocalisation(fetchLocalisationDependency(updateUserForm.getCountry(), updateUserForm.getCity()));
    }

    private Role fetchRoleDependency() {
        return roleRepository.findById(2L).orElse(null);
    }

    private Localisation fetchLocalisationDependency(final String country, final String city) {
        if(localisationRepository.existsByCountryAndCity(country, city)) {
            return localisationRepository.findLocalisationByCountryAndCity(country, city);
        }else {
            return Localisation.builder()
                    .country(country)
                    .city(city)
                    .build();

        }
    }

    @Override
    @Transactional
    public UserDto update(final UpdateUserForm updateUserForm) {
        final User user = userRepository.findUserByUuid(updateUserForm.getUuid())
                .orElseThrow(() -> new NotFoundException("User not found"));
        copyFromUpdateToEntity(updateUserForm, user);
        fetchUpdateUserDependencies(user, updateUserForm);
        final User newUser = userRepository.save(user);
        return UserMapper.toDto(newUser);
    }

    private void copyFromUpdateToEntity(final UpdateUserForm updateUserForm, final User user) {
        user.setUserName(updateUserForm.getUserName());
        user.setFirstName(updateUserForm.getFirstName());
        user.setLastName(updateUserForm.getLastName());
        user.setLanguages(updateUserForm.getLanguages());
        user.setDescription(updateUserForm.getDescription());
        user.setDateOfBirth(updateUserForm.getDateOfBirth());
    }
}
