package pwr.edu.pl.travelly.persistence.common;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SpecificationUtils {

    public static void addFromToPredicate(final CriteriaBuilder criteriaBuilder,
                                          final LocalDate beginDate,
                                          final LocalDate endDate,
                                          final Path<LocalDate> pathBeginDate,
                                          final Path<LocalDate> pathEndDate,
                                          final List<Predicate> predicateList) {

        if(Objects.nonNull(beginDate) && Objects.nonNull(endDate)){
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(pathBeginDate, beginDate ));
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(pathEndDate, endDate ));
        }else if(Objects.nonNull(beginDate)){
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(pathBeginDate, beginDate ));
        }else if(Objects.nonNull(endDate)){
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(pathEndDate, endDate ));
        }
    }

    public static void addDatePredicate(final CriteriaBuilder criteriaBuilder,
                                          final LocalDate date,
                                          final Path<LocalDate> path,
                                          final List<Predicate> predicateList) {
        if(Objects.nonNull(date)){
            predicateList.add(criteriaBuilder.equal(path, date));
        }
    }

    public static void isGreaterParticipants(final CriteriaBuilder criteriaBuilder, final Integer value, final Path<Integer> path, final List<Predicate> predicateList) {
        if (Objects.nonNull(value)) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(path,value));
        }
    }

    public static void addAuthorPredicate(final CriteriaBuilder criteriaBuilder, final UUID value, final Path<String> path, final List<Predicate> predicateList) {
        if (Objects.nonNull(value)) {
            predicateList.add(criteriaBuilder.equal(path,value));
        }
    }

    public static void addNotAuthorPredicate(final CriteriaBuilder criteriaBuilder, final UUID value, final Path<String> path, final List<Predicate> predicateList) {
        if (Objects.nonNull(value)) {
            predicateList.add(criteriaBuilder.notEqual(path,value));
        }
    }

    public static void addStatusPredicate(final CriteriaBuilder criteriaBuilder, final Boolean status, final Path<String> path, final List<Predicate> predicateList) {
        if (Objects.nonNull(status)) {
            predicateList.add(criteriaBuilder.equal(path, status));
        }
    }

    public static void isLike(final CriteriaBuilder criteriaBuilder, final String value, final Path<String> path, final List<Predicate> predicateList) {
        if (Objects.nonNull(value)) {
            predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%"));
        }
    }

}
