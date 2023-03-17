package ru.practicum.ewm.main.compilation.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.compilation.model.Compilation;

import java.util.Optional;


@Repository
public interface CompilationRepository extends PagingAndSortingRepository<Compilation, Long>,
        QuerydslPredicateExecutor<Compilation> {

    Compilation save(Compilation compilation);

    void deleteByCompilationId(Long compilationId);

    Optional<Compilation> findByCompilationId(Long compilationId);

    Boolean existsByCompilationId(Long compilationId);
}
