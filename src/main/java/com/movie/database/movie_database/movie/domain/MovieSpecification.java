package com.movie.database.movie_database.movie.domain;

import com.movie.database.movie_database.movie.model.MovieFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieSpecification implements Specification<Movie> {

    private final MovieFilter movieFilter;

    public MovieSpecification(MovieFilter movieFilter) {
        this.movieFilter = movieFilter;
    }

    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        movieFilter.getTitle().ifPresent(title -> predicates.add(getTitlePredicate(root, criteriaBuilder, title)));
        movieFilter.getDescription().ifPresent(description -> predicates.add(getDescriptionPredicate(root, criteriaBuilder, description)));
        movieFilter.getActors().ifPresent(actors -> predicates.add(getActorsPredicate(root, criteriaBuilder, actors)));
        movieFilter.getDirectors().ifPresent(directors -> predicates.add(getDirectorsPredicate(root, criteriaBuilder, directors)));
        movieFilter.getCategories().ifPresent(categories -> predicates.add(getCategoriesPredicate(root, criteriaBuilder, categories)));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate getTitlePredicate(Root<Movie> root, CriteriaBuilder criteriaBuilder, String title) {
        return criteriaBuilder.like(root.get("title"), prepareStringForLikeQuery(title));
    }

    private Predicate getDescriptionPredicate(Root<Movie> root, CriteriaBuilder criteriaBuilder, String description) {
        return criteriaBuilder.like(root.get("description"), prepareStringForLikeQuery(description));
    }

    private Predicate getActorsPredicate(Root<Movie> root, CriteriaBuilder criteriaBuilder, List<UUID> actors) {
        return criteriaBuilder.and(root.join("actors").get("id").in(actors));
    }

    private Predicate getDirectorsPredicate(Root<Movie> root, CriteriaBuilder criteriaBuilder, List<UUID> directors) {
        return criteriaBuilder.and(root.join("directors").get("id").in(directors));
    }

    private Predicate getCategoriesPredicate(Root<Movie> root, CriteriaBuilder criteriaBuilder, List<UUID> categories) {
        return criteriaBuilder.and(root.join("categories").get("id").in(categories));
    }

    private String prepareStringForLikeQuery(String phrase) {
        return StringUtils.wrap(phrase, "%");
    }
}
