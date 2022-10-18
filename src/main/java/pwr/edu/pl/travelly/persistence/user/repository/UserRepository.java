package pwr.edu.pl.travelly.persistence.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pwr.edu.pl.travelly.persistence.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    Optional<User> findUserByUserName(final String email);
    Optional<User> findUserByUuid(final UUID uuid);
    boolean existsByUserName(final String userName);
    boolean existsByUserNameAndUuidNot(final String email, final UUID uuid);
}
