package pwr.edu.pl.travelly.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pwr.edu.pl.travelly.persistence.user.entity.User;
import pwr.edu.pl.travelly.persistence.user.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByUserName_validUserName_returnsUser() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUserName("test@test.com");
        user.setPassword("Pa$$w0rd");
        userRepository.saveAndFlush(user);

        Optional<User> found = userRepository.findUserByUserName(user.getUserName());

        assertEquals(found.get().getUserName(), user.getUserName());
        assertEquals(found.get().getPassword(), user.getPassword());
    }

    @Test
    public void existsByUserName_validUserName_returnsTrue() {
        String userName = "test2@test.com";
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUserName(userName);
        user.setPassword("Pa$$w0rd");
        userRepository.saveAndFlush(user);

        assertTrue(userRepository.existsByUserName(userName));
        assertFalse(userRepository.existsByUserName("notexists"));
    }

    @Test
    public void existsByUserNameAndUuidNot_validUUID_returnsTrue() {
        String userName = "test3@test.com";
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUserName(userName);
        user.setPassword("Pa$$w0rd");
        userRepository.saveAndFlush(user);

        assertTrue(userRepository.existsByUserNameAndUuidNot(userName, UUID.randomUUID()));
    }
}
