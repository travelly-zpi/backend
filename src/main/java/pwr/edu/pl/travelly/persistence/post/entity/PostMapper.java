package pwr.edu.pl.travelly.persistence.post.entity;

import org.apache.commons.lang3.StringUtils;
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
//        if(!post.getAttachments().isEmpty()) {
//            final Optional<PostAttachment> first = post.getAttachments()
//                    .stream()
//                    .filter(PostAttachment::isMain)
//                    .findFirst();
//            return first.map(PostAttachment::getUrl).orElse(null);
//        }
        return null;
    }

    public static PostAttachmentDto toAttachmentDto(final PostAttachment attachment) {
        return PostAttachmentDto.builder()
                .attachmentUuid(attachment.getUuid())
                .isMain(attachment.isMain())
                .url(attachment.getUrl())
                .build();
    }

    private static List<String> getAttachments(final Post post) {
//        return post.getAttachments()
//                .stream()
//                .map(PostAttachment::getUrl)
//                .collect(Collectors.toList());
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
                .activeFrom(form.getActiveFrom() != null ? LocalDate.parse(form.getActiveFrom()) : null)
                .activeTo(form.getActiveTo() != null ? LocalDate.parse(form.getActiveTo()) : null)
                .type(form.getType())
                .creationTimestamp(LocalDateTime.now())
                .build();
    }

    public static PostListDto toListDto(final Post post) {
        return PostListDto.builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .mainImageUrl(null)
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
