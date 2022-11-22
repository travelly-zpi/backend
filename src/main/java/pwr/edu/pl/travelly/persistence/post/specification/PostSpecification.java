package pwr.edu.pl.travelly.persistence.post.specification;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import pwr.edu.pl.travelly.core.post.form.PostFilterForm;
import pwr.edu.pl.travelly.persistence.common.SpecificationUtils;
import pwr.edu.pl.travelly.persistence.post.entity.Post;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class PostSpecification implements Specification<Post> {

    private final PostFilterForm filterForm;

    @Override
    public Predicate toPredicate(final Root<Post> root, final CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicateList = new ArrayList<>();

        SpecificationUtils.isLike(criteriaBuilder, filterForm.getTitle(), root.get(Post.Fields.title), predicateList);
        SpecificationUtils.isLike(criteriaBuilder, filterForm.getStartPoint(), root.get(Post.Fields.startPoint), predicateList);
        SpecificationUtils.isLike(criteriaBuilder, filterForm.getEndPoint(), root.get(Post.Fields.endPoint), predicateList);
        SpecificationUtils.isLike(criteriaBuilder, filterForm.getType(), root.get(Post.Fields.type), predicateList);

        final LocalDate begin = Objects.isNull(filterForm.getStartDate()) ? null : LocalDate.parse(filterForm.getStartDate());
        final LocalDate end = Objects.isNull(filterForm.getEndDate()) ? null : LocalDate.parse(filterForm.getEndDate());
        SpecificationUtils.addFromToPredicate(criteriaBuilder, begin, end, root.get(Post.Fields.activeFrom), root.get(Post.Fields.activeTo) , predicateList);

        final LocalDate date = Objects.isNull(filterForm.getDate()) ? null : LocalDate.parse(filterForm.getDate());
        SpecificationUtils.addDatePredicate(criteriaBuilder, date, root.get(Post.Fields.activeFrom), predicateList);

        SpecificationUtils.isGreaterParticipants(criteriaBuilder, filterForm.getParticipants(), root.get(Post.Fields.participants), predicateList);
        SpecificationUtils.addStatusPredicate(criteriaBuilder, filterForm.getActive(), root.get(Post.Fields.active), predicateList);
        SpecificationUtils.addAuthorPredicate(criteriaBuilder, filterForm.getAuthor(), root.get(Post.Fields.author).get("uuid"), predicateList);
        SpecificationUtils.addNotAuthorPredicate(criteriaBuilder, filterForm.getNotAuthor(), root.get(Post.Fields.author).get("uuid"), predicateList);

        query.distinct(true);

        return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
    }
}
