package pwr.edu.pl.travelly.core.post.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.persistence.post.entity.Post;

import java.util.UUID;

public interface PostPort {
    Page<Post> findAll(final Pageable pageable, final PostFilterForm filterForm);
    PostDto findByUuid(final UUID uuid);
}
