package pwr.edu.pl.travelly.persistence.post.entity;

import pwr.edu.pl.travelly.core.post.dto.PostAttachmentDto;
import pwr.edu.pl.travelly.core.post.dto.PostAuthorDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.core.post.dto.PostListDto;
import pwr.edu.pl.travelly.core.post.form.SavePostForm;
import pwr.edu.pl.travelly.persistence.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .profileImageUrl(getMainAttachment(post))
                .images(getAttachments(post))
                .active(post.getActive())
                .postAuthorDto(toPostAuthorDto(post.getAuthor()))
                .build();
    }

    private static PostAttachmentDto getMainAttachment(final Post post) {
        if(Objects.nonNull(post.getAttachments()) && !post.getAttachments().isEmpty()) {
            final Optional<PostAttachment> mainImage = post.getAttachments()
                    .stream()
                    .filter(PostAttachment::isMain)
                    .findFirst();
            if(mainImage.isPresent()) {
                return toAttachmentDto(mainImage.get());
            }
        }
        return null;
    }

    private static String getMainAttachmentUrl(final PostAttachmentDto postAttachmentDto) {
        if(Objects.nonNull(postAttachmentDto)) {
            return postAttachmentDto.getUrl();
        }
        return null;
    }

    public static PostAttachmentDto toAttachmentDto(final PostAttachment attachment) {
        return PostAttachmentDto.builder()
                .attachmentUuid(attachment.getUuid())
                .isMain(attachment.isMain())
                .url(attachment.getUrl())
                .build();
    }

    private static List<PostAttachmentDto> getAttachments(final Post post) {
        if(Objects.nonNull(post.getAttachments()) && !post.getAttachments().isEmpty()) {
            return post.getAttachments()
                    .stream()
                    .filter(postAttachment->!postAttachment.isMain())
                    .map(PostMapper::toAttachmentDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static Post toEntity(final SavePostForm form) {
        return Post.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .startPoint(form.getStartPoint())
                .endPoint(form.getEndPoint())
                .participants(form.getParticipants())
                .active(form.getActive())
                .activeFrom(Objects.nonNull(form.getActiveFrom()) ? LocalDate.parse(form.getActiveFrom()) : null)
                .activeTo(Objects.nonNull(form.getActiveTo()) ? LocalDate.parse(form.getActiveTo()) : null)
                .type(form.getType())
                .creationTimestamp(LocalDateTime.now())
                .build();
    }

    public static PostListDto toListDto(final Post post) {
        final PostAttachmentDto mainAttachmentUrl = getMainAttachment(post);
        return PostListDto.builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .type(post.getType())
                .mainImage(getMainAttachmentUrl(mainAttachmentUrl))
                .description(post.getDescription())
                .build();
    }

    private static PostAuthorDto toPostAuthorDto(final User user) {
        return PostAuthorDto.builder()
                .uuid(user.getUuid())
                .email(user.getUserName())
                .build();
    }

}
