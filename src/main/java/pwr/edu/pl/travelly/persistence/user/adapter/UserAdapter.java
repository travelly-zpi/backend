package pwr.edu.pl.travelly.persistence.user.adapter;

import org.springframework.stereotype.Component;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.user.dto.LoggedUserDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;
import pwr.edu.pl.travelly.core.user.port.UserPort;
import pwr.edu.pl.travelly.persistence.user.entity.Role;
import pwr.edu.pl.travelly.persistence.user.entity.User;
import pwr.edu.pl.travelly.persistence.user.entity.UserMapper;
import pwr.edu.pl.travelly.persistence.user.repository.RoleRepository;
import pwr.edu.pl.travelly.persistence.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserAdapter(final UserRepository userRepository,
                       final RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
    public UserDto findByEmail(final String email) {
        final User user = userRepository
                .findUserByUserName(email)
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
    public void enableUser(String userName) {
        final User user = userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setEnable(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDto save(final CreateUserForm registerUserForm) {
        final User user = UserMapper.toEntity(registerUserForm);
        user.setUuid(UUID.randomUUID());
        fetchNewUserDependencies(user);
        user.setEnable(false);  //  set enable to false to new user
        final User newUser = userRepository.save(user);
        return UserMapper.toDto(newUser);
    }

    private void fetchNewUserDependencies(final User user) {
        user.setRole(fetchRoleDependency());
    }

    private Role fetchRoleDependency() {
        return roleRepository.findById(2L).orElse(null);
    }

    @Override
    @Transactional
    public UserDto update(final UpdateUserForm updateUserForm) {
        final User user = userRepository.findUserByUuid(updateUserForm.getUuid())
                .orElseThrow(() -> new NotFoundException("User not found"));
        copyFromUpdateToEntity(updateUserForm, user);
        final User newUser = userRepository.save(user);
        return UserMapper.toDto(newUser);
    }

    @Override
    @Transactional
    public String getUserPassword(final UUID userUuid) {
        final User user = userRepository.findUserByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return user.getPassword();
    }


    @Override
    @Transactional
    public void setNewUserPassword(final UUID userUuid, final String newPassword) {
        final User user = userRepository.findUserByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setPassword(newPassword);
    }

    private void copyFromUpdateToEntity(final UpdateUserForm updateUserForm, final User user) {
        user.setUserName(updateUserForm.getEmail());
        user.setFirstName(updateUserForm.getFirstName());
        user.setLastName(updateUserForm.getLastName());
        user.setLanguages(updateUserForm.getLanguages());
        user.setDescription(updateUserForm.getDescription());
        user.setDateOfBirth(LocalDate.parse(updateUserForm.getDateOfBirth()));
        user.setLocalisation(updateUserForm.getLocalisation());
    }
}
