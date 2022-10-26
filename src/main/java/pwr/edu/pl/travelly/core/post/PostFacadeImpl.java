package pwr.edu.pl.travelly.core.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.core.post.port.PostPort;
import pwr.edu.pl.travelly.persistence.post.entity.Post;

import java.util.UUID;

@Service("postFacade")
public class PostFacadeImpl implements PostFacade{

    private final PostPort postPort;

    public PostFacadeImpl(final PostPort postPort) {
        this.postPort = postPort;
    }

    @Override
    public Page<Post> findAll(final Pageable pageable, final PostFilterForm filterForm) {
        return postPort.findAll(pageable, filterForm);
    }

    @Override
    public PostDto findByUuid(final UUID uuid) {
        return postPort.findByUuid(uuid);
    }
}
