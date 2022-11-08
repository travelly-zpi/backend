package pwr.edu.pl.travelly.persistence.post.entity;

import pwr.edu.pl.travelly.core.common.exception.NotFoundException;
import pwr.edu.pl.travelly.core.post.dto.PostAttachmentDto;
import pwr.edu.pl.travelly.core.post.dto.PostAuthorDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.persistence.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostMapper {

    public static PostDto toDto(final Post post) {
        return PostDto.builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .description(post.getDescription())
                .startPoint(post.getStartPoint())
                .endPoint(post.getEndPoint())
                .participants(post.getParticipants())
                .activeFrom(post.getActiveFrom())
                .activeTo(post.getActiveTo())
                .type(post.getType())
                .creationTimestamp(post.getCreationTimestamp())
                .active(post.getActive())
                .profileImageUrl(getMainAttachmentUrl(post))
                .imagesUrls(getAttachments(post))
                .postAuthorDto(toPostAuthorDto(post.getAuthor()))
                .build();
    }

    private static String getMainAttachmentUrl(final Post post) {
        final Optional<PostAttachment> first = post.getAttachments()
                .stream()
                .filter(PostAttachment::isMain)
                .findFirst();
        return first.map(PostAttachment::getUrl).orElse(null);
    }

    public static PostAttachmentDto toAttachmentDto(final PostAttachment attachment) {
        return PostAttachmentDto.builder()
                .attachmentUuid(attachment.getUuid())
                .isMain(attachment.isMain())
                .url(attachment.getUrl())
                .build();
    }

    private static List<String> getAttachments(final Post post) {
        return post.getAttachments()
                .stream()
                .map(PostAttachment::getUrl)
                .collect(Collectors.toList());
    }

    public static Post toEntity(final SavePostForm form) {
        return Post.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .startPoint(form.getStartPoint())
                .endPoint(form.getEndPoint())
                .participants(form.getParticipants())
                .active(form.getActive())
                .activeFrom(LocalDate.parse(form.getActiveFrom()))
                .activeTo(LocalDate.parse(form.getActiveTo()))
                .type(form.getType())
                .creationTimestamp(LocalDateTime.now())
                .build();
    }

    public static PostListDto toListDto(final Post post) {
        return PostListDto.builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .mainImageUrl(getMainAttachmentUrl(post))
                .description(post.getDescription())
                .build();
    }

    private static PostAuthorDto toPostAuthorDto(final User user) {
        return PostAuthorDto.builder()
                .uuid(user.getUuid())
                .userName(user.getUserName())
                .build();
    }

}
