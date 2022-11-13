package pwr.edu.pl.travelly.core.post.port;

import org.springframework.data.domain.Pageable;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.persistence.post.entity.PostAttachment;

import java.util.List;
import java.util.UUID;

public interface PostPort {
    List<PostListDto> findAll(final Pageable pageable, final PostFilterForm filterForm);
    long count(final PostFilterForm filterForm);
    PostDto findByUuid(final UUID post);
    PostDto create(final SavePostForm savePostForm);
    PostDto update(final UUID uuid, final SavePostForm updatePostForm);
    void addAttachment(final UUID post, final PostAttachment attachment);
    String deleteAttachment(final UUID postUuid, final UUID attachmentUuid);
    void delete(final UUID post);
    void updateStatus(final UUID post, final Boolean status);
}
