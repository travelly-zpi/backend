package pwr.edu.pl.travelly.persistence.post.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import pwr.edu.pl.travelly.persistence.common.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "post_attachment")
public class PostAttachment extends AbstractEntity{

    @Column(name = "url")
    private String url;

    @Column(name = "profile_image")
    private boolean isMain;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
