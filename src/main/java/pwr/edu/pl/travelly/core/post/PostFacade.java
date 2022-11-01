package pwr.edu.pl.travelly.core.post;

import org.springframework.data.domain.Pageable;
import pwr.edu.pl.travelly.core.post.dto.BoardDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.form.CreatePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;

import java.io.IOException;
import java.util.UUID;

public interface PostFacade {
    BoardDto findAll(final Pageable pageable, final PostFilterForm filterForm);
    PostDto findByUuid(final UUID uuid);
    PostDto create(final CreatePostForm createPostForm) throws IOException;
    void activate(final UUID uuid);
    void deactivate(final UUID uuid);
}
