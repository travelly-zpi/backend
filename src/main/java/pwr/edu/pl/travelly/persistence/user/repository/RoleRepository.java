package pwr.edu.pl.travelly.persistence.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.edu.pl.travelly.persistence.user.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
