package pwr.edu.pl.travelly.core.post;

import org.springframework.data.domain.Pageable;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.CreatePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostFacade {
    List<PostListDto> findAll(final Pageable pageable, final PostFilterForm filterForm);
    PostDto findByUuid(final UUID uuid);
    PostDto create(final CreatePostForm createPostForm) throws IOException;
}
