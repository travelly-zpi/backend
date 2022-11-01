package pwr.edu.pl.travelly.persistence.post.specification;

import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
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


        SpecificationUtils.isLike(criteriaBuilder, filterForm.getStartPoint(), root.get(Post.Fields.startPoint), predicateList);
        SpecificationUtils.isLike(criteriaBuilder, filterForm.getEndPoint(), root.get(Post.Fields.endPoint), predicateList);

        final LocalDate begin = Objects.isNull(filterForm.getStartDate()) ? null : LocalDate.parse(filterForm.getStartDate());
        final LocalDate end = Objects.isNull(filterForm.getEndDate()) ? null : LocalDate.parse(filterForm.getEndDate());
        SpecificationUtils.addFromToPredicate(criteriaBuilder, begin, end, root.get(Post.Fields.activeFrom), root.get(Post.Fields.activeTo) , predicateList);

        SpecificationUtils.isEqualParticipants(criteriaBuilder, filterForm.getParticipants(), root.get(Post.Fields.participants), predicateList);

        query.distinct(true);

        return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
    }
}
