package ru.practicum.service.compilation.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.service.compilation.model.Compilation;


@Repository
public interface CompilationRepository extends PagingAndSortingRepository<Compilation, Long>,
        QuerydslPredicateExecutor<Compilation> {
}
