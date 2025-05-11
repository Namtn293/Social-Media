package com.namtn.media.core.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.aspectj.weaver.Lint;
import org.springframework.data.jpa.domain.Specification;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Objects;

public class SearchUtil {
    //create conditions for query
    public static <T> Specification<T> eq(String fieldName,Comparable value){
        return ((root, query, criteriaBuilder) -> {
            return value!= null ? criteriaBuilder.equal(root.get(fieldName), value) : criteriaBuilder.conjunction();
        });
    }

    public static <T> Specification<T> le(String fieldName,Comparable value){
        return ((root, query, criteriaBuilder) -> {
            return value!= null ? criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), value) : criteriaBuilder.conjunction();
        });
    }

    public static <T> Specification<T> ge(String fieldName,Comparable value){
        return ((root, query, criteriaBuilder) -> {
            return value!= null ? criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), value) : criteriaBuilder.conjunction();
        });
    }

    public static <R,F> Specification<R> in(String fieldName, List<F> filterList){
        return (root, query, criteriaBuilder) ->{
            if (!filterList.isEmpty() && filterList!=null){
                if (filterList.size()>1){
                    CriteriaBuilder.In<F> inClause=criteriaBuilder.in(root.get(fieldName));
                    Objects.requireNonNull(inClause);
                    filterList.forEach(inClause::value);
                    return inClause;
                } else {
                        return criteriaBuilder.equal(root.get(fieldName),filterList.get(0));
                }
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static<T> Specification<T> like(String fieldName,String value){
        return ((root, query, criteriaBuilder) -> {
            return value!=null ? criteriaBuilder.like(root.get(fieldName),"%"+value.trim()+"%"):criteriaBuilder.conjunction();
        });
    }
    public static<T> Specification<T> newSpecification(){
        return (((root, query, criteriaBuilder) -> {
            return criteriaBuilder.conjunction();
        }));
    }
}
