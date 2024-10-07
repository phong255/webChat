package dev.phong.webChat.util;

import jakarta.persistence.criteria.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class QueryUtils {
    public static String buildLikeExp(final String query) {
        if (query == null || !H.isTrue(query.trim())) {
            return null;
        }
        return "%" + query.trim()
//                .replaceAll("\\s+", "%")
                + "%";
    }

    public static <T> Predicate buildLikeFilter(final Root<T> root, final CriteriaBuilder cb, final String keyword, final String... fieldNames) {
        final String likeExp = buildLikeExp(keyword);
        if (!H.isTrue(likeExp) || !H.isTrue(fieldNames)) {
            return cb.and();
        }
        return cb.or(H.collect(Arrays.asList(fieldNames), (index, fieldName) -> cb.like(cb.upper(root.get(fieldName)), likeExp.toUpperCase())).toArray(new Predicate[0]));
    }

    public static <T, P> Predicate buildNotEqFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final P value) {
        return H.isTrue(value) ? cb.notEqual(root.get(fieldName), value) : cb.and();
    }

    public static <T> Predicate buildLikeFilter(final Root<T> root, final CriteriaBuilder cb, final String keyword, final Path... paths) {
        final String likeExp = buildLikeExp(keyword);
        if (!H.isTrue(likeExp)) {
            return cb.and();
        }
        return cb.or(H.collect(Arrays.asList(paths), (index, path) -> cb.like(cb.upper((Expression<String>) path), likeExp.toUpperCase())).toArray(new Predicate[0]));
    }

    public static <T> Predicate buildSimpleLikeFilter(final Root<T> root, final CriteriaBuilder cb, final String keyword, final String... fieldNames) {
        if (!H.isTrue(keyword) || !H.isTrue(fieldNames)) {
            return cb.and();
        }
        return cb.or(H.collect(Arrays.asList(fieldNames), (index, fieldName) -> cb.like(cb.upper(root.get(fieldName)), ("%" + keyword + "%").toUpperCase())).toArray(new Predicate[0]));
    }

    public static <T, P> Predicate buildEqFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final P value) {
        return H.isTrue(value) ? cb.equal(root.get(fieldName), value) : cb.and();
    }

    public static <T, P> Predicate buildEqFilter(final Path<T> fieldName, final CriteriaBuilder cb, final P value) {
        return H.isTrue(value) ? cb.equal(fieldName, value) : cb.and();
    }

    public static <T, P> Predicate buildEqFilter(final Root<T> root, final CriteriaBuilder cb, final Path fieldName, final P value) {
        return H.isTrue(value) ? cb.equal(fieldName, value) : cb.and();
    }

    public static <T> Predicate buildIsDeleteFilter(final Root<T> root, final CriteriaBuilder cb) {
        return cb.equal(root.get("isDelete"), false);
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final Collection<P> values) {
        return H.isTrue(values) ? root.get(fieldName).in(values) : cb.and();
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, Path path, final Collection<P> values) {
        return H.isTrue(values) ? path.in(values) : cb.and();
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, Path path, String values) {
        return H.isTrue(values) ? path.in(values.split(",")) : cb.and();
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, String values) {
        return H.isTrue(values) ? root.get(fieldName).in((Object[]) values.split(",")) : cb.and();
    }


    public static <T, P> Predicate buildDateFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, String value, String pattern) {
        return H.isTrue(value) ?
                cb.equal(
                        cb.function("DATE", Date.class, root.get(fieldName)),
                        cb.function("STR_TO_DATE", Date.class, cb.literal(value), cb.literal(pattern)))
                : cb.and();
    }

    public static <T, P> Predicate buildEq(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final P value) {
        return cb.equal(root.get(fieldName), value);
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, Subquery<?> subquery) {
        return H.isTrue(subquery) ? root.get(fieldName).in(subquery) : cb.and();
    }


    @SneakyThrows
    public static <T, P> Predicate buildBetweenFilter(final Root<T> root, final CriteriaBuilder cb,
                                                      final String fieldName, String valueFrom, String valueEnd, String pattern) {

        return H.isTrue(valueFrom) && H.isTrue(valueEnd) ?
                cb.between(cb.function("TRUNC", Date.class, root.get(fieldName)),
                        DateUtils.parseDate(valueFrom, pattern), DateUtils.parseDate(valueEnd, pattern)) : cb.and();
    }

    public static Predicate buildBetweenFilter1(
            Root<?> root, CriteriaBuilder cb, String field, Date fromValue, Date toValue) {
        if (fromValue != null && toValue != null) {
            return cb.between(root.get(field), fromValue, toValue);
        } else if (fromValue != null) {
            return cb.greaterThanOrEqualTo(root.get(field), fromValue);
        } else if (toValue != null) {
            return cb.lessThanOrEqualTo(root.get(field), toValue);
        } else {
            return cb.and();
        }
    }

    public static <T,P> Predicate joinBuildEq(final Root<T> root, final CriteriaBuilder cb, String fieldName,final P value, String tableName){
        Join<T,P> tableJoin = root.join(tableName);
        if (H.isTrue(value))
            return cb.equal(tableJoin.get(fieldName), value);
        else return cb.and();
    }

    public static <T, P> Predicate joinAndLike(final Root<T> root, final CriteriaBuilder cb, String fieldName, String value, String tableName) {
        Join<T, P> tableJoin = root.join(tableName);
        if (!H.isTrue(value))
            return cb.and();
        return cb.like(cb.upper(tableJoin.get(fieldName)), QueryUtils.buildLikeExp(value.toUpperCase()));
    }

    public static <T, P> Predicate joinThreeTableAndLike(final Root<T> root, final CriteriaBuilder cb, String fieldName, String value, String tableName1, String tableName2) {
        Join<T, P> tableJoin = root.join(tableName1).join(tableName2);
        if (!H.isTrue(value))
            return cb.and();
        return cb.like(cb.upper(tableJoin.get(fieldName)), QueryUtils.buildLikeExp(value.toUpperCase()));
    }

    public static <T,P> Predicate joinThreeBuildEq(final Root<T> root, final CriteriaBuilder cb, String fieldName,final P value, String tableName1, String tableName2){
        Join<T,P> tableJoin = root.join(tableName1).join(tableName2);
        if (H.isTrue(value))
            return cb.equal(tableJoin.get(fieldName), value);
        else return cb.and();
    }

}