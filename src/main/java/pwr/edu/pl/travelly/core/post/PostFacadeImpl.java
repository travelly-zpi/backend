package pwr.edu.pl.travelly.core.post;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pwr.edu.pl.travelly.core.post.dto.BoardDto;
import pwr.edu.pl.travelly.core.post.dto.PostAttachmentDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.core.post.port.PostPort;
import pwr.edu.pl.travelly.persistence.post.entity.PostAttachment;
import pwr.edu.pl.travelly.persistence.post.entity.PostMapper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service("postFacade")
public class PostFacadeImpl implements PostFacade{

    private final PostPort postPort;
    private BlobContainerClient blobContainer;

    private final String AZURE_CONNECTION = "AccountName=travelly;" +
            "AccountKey=GdmI12VBEQ8CzuVV9ezLbkXX2NYosROQnvz40eKXvU9e6AUa6eRFdsKkIkwhYYzYrhRK/HYGBJEB+AStxFhdwg==;" +
            "EndpointSuffix=core.windows.net;" +
            "DefaultEndpointsProtocol=https;";

    public PostFacadeImpl(final PostPort postPort) {
        this.postPort = postPort;
        this.createBlobContainer();
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
    public PostDto create(final SavePostForm savePostForm) {
        return postPort.create(savePostForm);
    }

    @Override
    public PostDto update(final UUID uuid, final SavePostForm updatePostForm) {
        return postPort.update(uuid, updatePostForm);
    }

    @Override
    public void deleteAttachment(final UUID postUuid, final UUID attachmentUuid) {
        final String attachmentName = postPort.deleteAttachment(postUuid, attachmentUuid);
        deleteImage(attachmentName);
    }

    private void deleteImage(final String attachmentName) {
        final BlobClient imageBlob = this.blobContainer.getBlobClient(attachmentName);
        imageBlob.delete();
    }

    @Override
    public PostAttachmentDto uploadAttachment(final MultipartFile image, final UUID postUuid, final Boolean status) throws IOException {
        final PostDto post = findByUuid(postUuid);
        final PostAttachment newAttachment = uploadImage(image, post, status);
        postPort.addAttachment(post.getUuid(), newAttachment);
        return PostMapper.toAttachmentDto(newAttachment);
    }

    private PostAttachment uploadImage(final MultipartFile image, final PostDto post, final boolean isMain) throws IOException {
        final UUID imageUuid = UUID.randomUUID();
        final String attachmentName = generatePostAttachmentImageName(post.getUuid(), imageUuid);

        final BlobClient imageBlob = this.blobContainer.getBlobClient(attachmentName);
        imageBlob.upload(image.getInputStream(), image.getSize(), true);

        return createPostAttachment(imageUuid, attachmentName, isMain);
    }

    private PostAttachment createPostAttachment(final UUID imageUUID, final String attachmentName, final boolean isMain) {
        final PostAttachment attachment = new PostAttachment();
        attachment.setUuid(imageUUID);
        attachment.setMain(isMain);
        attachment.setUrl(attachmentName);
        return attachment;
    }

    @Override
    public void delete(final UUID uuid) {
        postPort.delete(uuid);
    }

    @Override
    public void updateStatus(final UUID uuid, final Boolean status) {
        postPort.updateStatus(uuid, status);
    }

    private void createBlobContainer() {
        this.blobContainer = new BlobContainerClientBuilder()
                .connectionString(AZURE_CONNECTION)
                .containerName("images")
                .buildClient();
    }

    private String generatePostAttachmentImageName(final UUID postUuid, final UUID imageUuid) {
        return String.join("_","post",postUuid.toString(),"attachment",imageUuid.toString());
    }
}
