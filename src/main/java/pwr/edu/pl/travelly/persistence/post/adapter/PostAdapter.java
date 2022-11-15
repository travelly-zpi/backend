package pwr.edu.pl.travelly.persistence.post.adapter;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.core.post.port.PostPort;
import pwr.edu.pl.travelly.persistence.post.entity.Post;
import pwr.edu.pl.travelly.persistence.post.entity.PostAttachment;
import pwr.edu.pl.travelly.persistence.post.entity.PostMapper;
import pwr.edu.pl.travelly.persistence.post.repository.PostAttachmentRepository;
import pwr.edu.pl.travelly.persistence.post.repository.PostRepository;
import pwr.edu.pl.travelly.persistence.post.specification.PostSpecification;
import pwr.edu.pl.travelly.persistence.user.entity.User;
import pwr.edu.pl.travelly.persistence.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostAdapter implements PostPort {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostAttachmentRepository postAttachmentRepository;

    public PostAdapter(final PostRepository postRepository,
                       final UserRepository userRepository,
                       final PostAttachmentRepository postAttachmentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postAttachmentRepository = postAttachmentRepository;
    }

    @Override
    @Transactional
    public List<PostListDto> findAll(final Pageable pageable, final PostFilterForm filterForm) {
        return postRepository.findAll(new PostSpecification(filterForm), pageable)
                .stream()
                .map(PostMapper::toListDto).collect(Collectors.toList());
    }

    @Override
    public long count(final PostFilterForm filterForm) {
        return postRepository.count(new PostSpecification(filterForm));
    }

    @Override
    @Transactional
    public PostDto findByUuid(final UUID uuid) {
        final Post post = postRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        return PostMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostDto create(final SavePostForm savePostForm) {
        final Post post = PostMapper.toEntity(savePostForm);
        post.setUuid(UUID.randomUUID());
        post.setAuthor(getUserByUuid(savePostForm.getAuthor()));
        return PostMapper.toDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDto update(final UUID uuid, final SavePostForm updatePostForm) {
        final Post post = postRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        updatePost(post, updatePostForm);
        return PostMapper.toDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public void addAttachment(final UUID postUuid, final PostAttachment attachment) {
        final Post post = postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        attachment.setPost(post);
        post.getAttachments().add(attachment);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public String deleteAttachment(final UUID postUuid, final UUID attachmentUuid) {
        final Post post = postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        final PostAttachment attachment = postAttachmentRepository.findByUuid(attachmentUuid)
                .orElseThrow(() -> new NotFoundException("POST_ATTACHMENT_NOT_FOUND"));
        final String attachmentUrl = attachment.getUrl();
        post.getAttachments().remove(attachment);
        postRepository.save(post);
        return attachmentUrl;
    }

    @Override
    @Transactional
    public void delete(final UUID postUuid) {
        final Post post = postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void updateStatus(final UUID postUuid, final Boolean status) {
        final Post post = postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        post.setActive(status);
        postRepository.save(post);
    }

    private User getUserByUuid(final UUID uuid) {
        return userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND_USER"));
    }

    private void updatePost(final Post post, final SavePostForm savePostForm) {
        post.setTitle(savePostForm.getTitle());
        post.setDescription(savePostForm.getDescription());
        post.setStartPoint(savePostForm.getStartPoint());
        post.setEndPoint(savePostForm.getEndPoint());
        post.setParticipants(savePostForm.getParticipants());
        post.setActiveFrom(Objects.nonNull(savePostForm.getActiveFrom()) ? LocalDate.parse(savePostForm.getActiveFrom()) : null);
        post.setActiveTo(Objects.nonNull(savePostForm.getActiveTo()) ? LocalDate.parse(savePostForm.getActiveTo()) : null);
        post.setType(savePostForm.getType());
    }
}

