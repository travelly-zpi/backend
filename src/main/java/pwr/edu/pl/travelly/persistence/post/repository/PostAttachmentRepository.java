package pwr.edu.pl.travelly.persistence.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.edu.pl.travelly.persistence.post.entity.PostAttachment;

import java.util.Optional;
import java.util.UUID;

public interface PostAttachmentRepository extends JpaRepository<PostAttachment,Long> {
    Optional<PostAttachment> findByUuid(final UUID uuid);
}
