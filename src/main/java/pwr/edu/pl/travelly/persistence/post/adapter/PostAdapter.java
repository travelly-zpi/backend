package pwr.edu.pl.travelly.persistence.post.adapter;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.CreatePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.core.post.port.PostPort;
import pwr.edu.pl.travelly.persistence.post.entity.Post;
import pwr.edu.pl.travelly.persistence.post.entity.PostAttachment;
import pwr.edu.pl.travelly.persistence.post.entity.PostMapper;
import pwr.edu.pl.travelly.persistence.post.repository.PostRepository;
import pwr.edu.pl.travelly.persistence.post.specification.PostSpecification;
import pwr.edu.pl.travelly.persistence.user.entity.User;
import pwr.edu.pl.travelly.persistence.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostAdapter implements PostPort {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private BlobContainerClient blobContainer;

    private final String AZURE_CONNECTION = "AccountName=travelly;" +
            "AccountKey=GdmI12VBEQ8CzuVV9ezLbkXX2NYosROQnvz40eKXvU9e6AUa6eRFdsKkIkwhYYzYrhRK/HYGBJEB+AStxFhdwg==;" +
            "EndpointSuffix=core.windows.net;" +
            "DefaultEndpointsProtocol=https;";

    public PostAdapter(final PostRepository postRepository,
                       final UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
    public PostDto create(final CreatePostForm createPostForm) throws IOException {
        this.createBlobContainer();
        final Post post = PostMapper.toEntity(createPostForm);
        post.setUuid(UUID.randomUUID());
        post.setAuthor(getUserByUuid(createPostForm.getAuthor()));
        uploadImage(createPostForm.getProfileImage(), post, true);
        uploadAttachments(createPostForm.getImages(), post);
        return PostMapper.toDto(postRepository.save(post));
    }

    private User getUserByUuid(final UUID uuid) {
        return userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND_USER"));
    }

    private void createBlobContainer() {
        this.blobContainer = new BlobContainerClientBuilder()
                .connectionString(AZURE_CONNECTION)
                .containerName("images")
                .buildClient();
    }

    private void uploadAttachments(final List<MultipartFile> images, final Post post) {
        if(Objects.nonNull(images)) {
            images.forEach(image-> {
                try {
                    uploadImage(image, post, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void uploadImage(final MultipartFile image, final Post post, final boolean isMain) throws IOException {
        if(Objects.nonNull(image)) {
            final UUID imageUuid = UUID.randomUUID();
            final String attachmentName = generatePostAttachmentImageName(post.getUuid(), imageUuid);
            final PostAttachment postAttachment = createPostAttachment(imageUuid, attachmentName, isMain);

            post.addAttachment(postAttachment);
            postAttachment.setPost(post);
            BlobClient blob = this.blobContainer.getBlobClient(attachmentName);
            blob.upload(image.getInputStream(), image.getSize(), true);
        }
    }

    private PostAttachment createPostAttachment(final UUID imageUUID, final String attachmentName, final boolean isMain) {
        final PostAttachment attachment = new PostAttachment();
        attachment.setUuid(imageUUID);
        attachment.setMain(isMain);
        attachment.setUrl(attachmentName);
        return attachment;
    }

    private String generatePostAttachmentImageName(final UUID postUuid, final UUID imageUuid) {
        return String.join("_","post",postUuid.toString(),"attachment",imageUuid.toString());
    }
}

