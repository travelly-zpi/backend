package pwr.edu.pl.travelly.persistence.common;

import pwr.edu.pl.travelly.persistence.post.entity.Post;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class SpecificationUtils {

    public static void addLikePredicate(final CriteriaBuilder criteriaBuilder, final String value, Path<String> path, final List<Predicate> predicateList) {
        if (Objects.nonNull(value)) {
            predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%"));
        }
    }

    public static void addFromToPredicate(final CriteriaBuilder criteriaBuilder,
                                          final LocalDate beginDate,
                                          final LocalDate endDate,
                                          final Path<LocalDate> pathBeginDate,
                                          final Path<LocalDate> pathEndDate,
                                          final List<Predicate> predicateList) {

        if(Objects.nonNull(beginDate) && Objects.nonNull(endDate)){
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(pathBeginDate, endDate ));
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(pathEndDate, beginDate ));
        }else if(Objects.nonNull(beginDate)){
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(pathBeginDate, beginDate ));
        }else if(Objects.nonNull(endDate)){
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(pathEndDate, endDate ));
        }
    }

    public static void isLike(final CriteriaBuilder criteriaBuilder, final String value, final Root<Post> root, final String field, final List<Predicate> predicateList) {
        if (Objects.nonNull(value)) {
            predicateList.add(criteriaBuilder.like(root.get(field), "%" + value + "%"));
        }
    }

}
