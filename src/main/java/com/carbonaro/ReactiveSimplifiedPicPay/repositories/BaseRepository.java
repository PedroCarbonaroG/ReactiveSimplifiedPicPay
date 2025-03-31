package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Repository
public abstract class BaseRepository {

    @Autowired
    protected ReactiveMongoTemplate template;

    private static final String SEARCH_BY_APPROXIMATION_AND_CASE_INSENSITIVE = "i";

    protected <T> Mono<Page<T>> toPage(Query query, Pageable page, Class<T> clazz) {

        return template
                .count(query, clazz)
                .flatMap(total -> template
                        .find(query.with(page), clazz)
                        .collectList()
                        .map(list -> new PageImpl<>(list, page, total)));
    }

    protected <T> void addParamToQuery(Query query, String fieldName, T fieldValue) {
        if (possibleAddParamToQuery(query, fieldName, fieldValue)) {
            if (fieldValue instanceof String) { query.addCriteria(Criteria.where(fieldName).regex(fieldValue.toString(), SEARCH_BY_APPROXIMATION_AND_CASE_INSENSITIVE)); }
            else { query.addCriteria(Criteria.where(fieldName).is(fieldValue)); }
        }
    }

    protected void addParamBetweenDates(Query query, String field, LocalDate initialDate, LocalDate finalDate) {
        if (possibleAddParamToQuery(query, field) && possibleAddParamToQuery(initialDate) || possibleAddParamToQuery(query, field, finalDate)) {
            query.addCriteria(Criteria.where(field).gte(initialDate).lte(finalDate));
        }
    }

    protected void addParamBetweenValues(Query query, String field, BigDecimal initialValue, BigDecimal finalValue) {

        if (initialValue != null || finalValue != null) {
            Criteria criteria = Criteria.where(field);
            if (initialValue != null && finalValue != null) { criteria.gte(initialValue).lte(finalValue); }
            else if (initialValue != null) { criteria.gte(initialValue); }
            else { criteria.lte(finalValue); }
            query.addCriteria(criteria);
        }
    }

    private boolean possibleAddParamToQuery(Object... params) {
        return Stream.of(params).allMatch(Objects::nonNull);
    }

    @SneakyThrows
    protected <T> T copyPropertiesForNullFields(T source, T target) {

        var fields = Stream.concat(
                Stream.of(source.getClass().getDeclaredFields()),
                Stream.of(source.getClass().getSuperclass().getDeclaredFields())
        ).toList();

        for (Field field : fields) {

            field.setAccessible(true);
            if (Objects.isNull(field.get(target))) {
                field.set(target, field.get(source));
            }
        }

        return target;
    }

}