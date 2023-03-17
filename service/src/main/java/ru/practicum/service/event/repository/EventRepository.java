package ru.practicum.service.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.State;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiatorUserId(Long userId, Pageable pageable);

    Optional<Event> findByEventIdAndInitiatorUserId(Long eventId, Long userId);

    Set<Event> findAllByEventIdIn(List<Long> eventIds);

    Optional<Event> findByEventIdAndState(Long eventId, State state);
}
