package pwr.edu.pl.travelly.persistence.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pwr.edu.pl.travelly.persistence.post.entity.Post;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post,Long>, JpaSpecificationExecutor<Post> {
    Optional<Post> findByUuid(final UUID uuid);
}
