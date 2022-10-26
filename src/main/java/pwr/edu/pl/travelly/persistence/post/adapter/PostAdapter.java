package pwr.edu.pl.travelly.persistence.post.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.core.post.port.PostPort;
import pwr.edu.pl.travelly.persistence.post.entity.Post;
import pwr.edu.pl.travelly.persistence.post.entity.PostMapper;
import pwr.edu.pl.travelly.persistence.post.repository.PostRepository;
import pwr.edu.pl.travelly.persistence.post.specification.PostSpecification;

import java.util.UUID;

@Service
public class PostAdapter implements PostPort {

    private final PostRepository postRepository;

    public PostAdapter(final PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findAll(Pageable pageable, PostFilterForm filterForm) {
        return postRepository.findAll(new PostSpecification(filterForm), pageable);
    }

    @Override
    public PostDto findByUuid(final UUID uuid) {
        final Post post = postRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        return PostMapper.toDto(post);
    }
}
