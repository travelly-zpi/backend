package pwr.edu.pl.travelly.persistence.user.adapter;

import org.springframework.stereotype.Component;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.port.UserPort;
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

    public UserAdapter(final UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserDto findByUserName(String userName) {
        final User user = userRepository
                .findUserByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public boolean existsByUuid(final UUID uuid) {
        return userRepository.existsByUuid(uuid);
    }

    @Override
    @Transactional
    public boolean existsByEmailAndUuidNot(final String email, final UUID uuid) {
        return userRepository.existsByEmailAndUuidNot(email, uuid);
    }

    @Override
    @Transactional
    public UserDto findByEmail(final String email) {
        final User user = userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto save(final CreateUserForm registerUserForm) {
        final User user = UserMapper.toEntity(registerUserForm);
        user.setUuid(UUID.randomUUID());
        user.setRole(roleRepository.findById(2L).get());
        final User newUser = userRepository.save(user);
        return UserMapper.toDto(newUser);
    }
}
