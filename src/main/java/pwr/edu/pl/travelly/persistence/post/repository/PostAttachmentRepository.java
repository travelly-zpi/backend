package pwr.edu.pl.travelly.persistence.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.edu.pl.travelly.persistence.post.entity.PostAttachment;

import java.util.Optional;
import java.util.UUID;

public interface PostAttachmentRepository extends JpaRepository<PostAttachment,Long> {
    Optional<PostAttachment> findByUuid(final UUID uuid);
//
//    @Query("SELECT postAttachment FROM PostAttachment postAttachment " +
//            "WHERE postAttachment.postId = :post_id and postAttachment.isMain=true")
//    Optional<PostAttachment> findMainByPostId(@Param("post_id")final Long id);
}
