package pwr.edu.pl.travelly.persistence.post.entity;

import pwr.edu.pl.travelly.core.post.dto.PostAuthorDto;
import pwr.edu.pl.travelly.core.post.dto.PostDto;
import pwr.edu.pl.travelly.persistence.user.entity.User;

public class PostMapper {

    public static PostDto toDto(final Post post) {
        return PostDto.builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .description(post.getDescription())
                .localisation(post.getLocalisation())
                .people(post.getPeople())
                .activeFrom(post.getActiveFrom())
                .activeTo(post.getActiveTo())
                .creationTimestamp(post.getCreationTimestamp())
                .postAuthorDto(toPostAuthorDto(post.getUser()))
                .build();
    }

    private static PostAuthorDto toPostAuthorDto(final User user) {
        return PostAuthorDto.builder()
                .uuid(user.getUuid())
                .userName(user.getUserName())
                .build();
    }

}
