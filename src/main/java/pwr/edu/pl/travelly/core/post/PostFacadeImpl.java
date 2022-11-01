package pwr.edu.pl.travelly.core.post;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pwr.edu.pl.travelly.core.post.dto.BoardDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.CreatePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.core.post.port.PostPort;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service("postFacade")
public class PostFacadeImpl implements PostFacade{

    private final PostPort postPort;

    public PostFacadeImpl(final PostPort postPort) {
        this.postPort = postPort;
    }

    @Override
    public BoardDto findAll(final Pageable pageable, final PostFilterForm filterForm) {
        final List<PostListDto> posts = postPort.findAll(pageable, filterForm);
        final long count = postPort.count(filterForm);
        final long pages = count<(long)pageable.getPageSize() ? 1 : count/(long)pageable.getPageSize();
        return BoardDto.builder()
                .posts(posts)
                .count(count)
                .pages(pages).build();
    }

    @Override
    public PostDto findByUuid(final UUID uuid) {
        return postPort.findByUuid(uuid);
    }

    @Override
    public PostDto create(final CreatePostForm createPostForm) throws IOException {
        return postPort.create(createPostForm);
    }
}
