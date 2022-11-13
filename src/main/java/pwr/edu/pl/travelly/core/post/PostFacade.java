package pwr.edu.pl.travelly.core.post;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pwr.edu.pl.travelly.core.post.dto.BoardDto;
import pwr.edu.pl.travelly.core.post.dto.PostAttachmentDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;

import java.io.IOException;
import java.util.UUID;

public interface PostFacade {
    BoardDto findAll(final Pageable pageable, final PostFilterForm filterForm);
    PostDto findByUuid(final UUID uuid);
    PostDto create(final SavePostForm savePostForm);
    PostDto update(final UUID uuid, final SavePostForm updatePostForm);
    void deleteAttachment(final UUID postUuid, final UUID AttachmentUuid);
    PostAttachmentDto uploadAttachment(final MultipartFile image, final UUID postUuid, final Boolean status) throws IOException;
    void delete(final UUID uuid);
    void updateStatus(final UUID uuid, final Boolean status);
}
