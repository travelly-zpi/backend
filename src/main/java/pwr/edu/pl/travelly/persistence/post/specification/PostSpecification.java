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
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PostSpecification implements Specification<Post> {

    private final PostFilterForm filterForm;

    @Override
    public Predicate toPredicate(final Root<Post> root, final CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicateList = new ArrayList<>();

        SpecificationUtils.isLike(criteriaBuilder, filterForm.getLocalisation(), root, Post.Fields.localisation, predicateList);
        SpecificationUtils.addFromToPredicate(criteriaBuilder, filterForm.getStartDate(), filterForm.getEndDate(), root.get(Post.Fields.activeFrom), root.get(Post.Fields.activeTo) , predicateList);

        return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
    }
}
