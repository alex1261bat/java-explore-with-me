package ru.practicum.ewm.main.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Request save(Request request);

    Optional<Request> findByRequestIdIs(Long requestId);

    List<Request> findAllByRequesterUserId(Long userId);

    List<Request> findAllByRequestIdIn(List<Long> requestIds);
}
