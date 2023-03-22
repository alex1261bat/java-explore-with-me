package ru.practicum.service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.request.model.Request;
import ru.practicum.service.request.model.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterUserId(Long userId);

    List<Request> findAllByRequestIdIn(List<Long> requestIds);

    List<Request> findAllByEventIs(Event event);

    List<Request> findAllByEventIsAndStatusIs(Event event, RequestStatus requestStatus);
}
